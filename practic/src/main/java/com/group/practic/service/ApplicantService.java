package com.group.practic.service;

import com.group.practic.dto.SendMessageDto;
import com.group.practic.entity.ApplicantEntity;
import com.group.practic.entity.CourseEntity;
import com.group.practic.entity.PersonEntity;
import com.group.practic.repository.ApplicantRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApplicantService {

    private ApplicantRepository applicantRepository;

    EmailSenderService emailSenderService;

    
    @Autowired
    public ApplicantService(ApplicantRepository applicantRepository,
            EmailSenderService emailSenderService) {
        this.applicantRepository = applicantRepository;
        this.emailSenderService = emailSenderService;
    }


    public Optional<ApplicantEntity> get(long id) {
        return applicantRepository.findById(id);
    }


    public List<ApplicantEntity> get(CourseEntity course, boolean isApplied) {
        return applicantRepository.findAllByCourseAndIsApplied(course, isApplied);
    }


    public Optional<ApplicantEntity> get(PersonEntity person, CourseEntity course) {
        return applicantRepository.findByPersonAndCourse(person, course);
    }


    public Optional<ApplicantEntity> create(PersonEntity person, CourseEntity course) {
        String slug = course.getSlug();
        return person.getMentors().stream().anyMatch(mentor -> mentor.getSlug().equals(slug)) 
                || applicantRepository.findByPersonAndCourse(person, course).isPresent()
                        ? Optional.empty()
                        : Optional.of(applicantRepository.save(new ApplicantEntity(person, course)));
    }


    public ApplicantEntity apply(ApplicantEntity applicant) {
        applicant.setApplied(true);
        applicant = applicantRepository.save(applicant);
        SendMessageDto messageDto = new SendMessageDto();
        messageDto.setAddress(applicant.getPerson().getEmail());
        messageDto.setHeader("Заявку на навчання прийнято!");
        messageDto.setMessage("Вітаємо на курсі \"" + applicant.getCourse().getName() + "\"");
        this.emailSenderService.sendMessage(messageDto);
        return applicant;
    }


    public boolean reject(ApplicantEntity applicant) {
        long id = applicant.getId();
        SendMessageDto messageDto = new SendMessageDto();
        messageDto.setAddress(applicant.getPerson().getEmail());
        messageDto.setHeader("Заявку на навчання відхилено!");
        messageDto.setMessage(
                "Відмова навчання на курсі \"" + applicant.getCourse().getName() + "\"");
        applicantRepository.delete(applicant);
        this.emailSenderService.sendMessage(messageDto);
        return !applicantRepository.existsById(id);
    }

}
