package com.group.practic.service;

import com.group.practic.entity.ApplicantEntity;
import com.group.practic.entity.CourseEntity;
import com.group.practic.entity.PersonEntity;
import com.group.practic.repository.ApplicantRepository;
import java.util.Collection;
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


    public void save(ApplicantEntity applicant) {
        applicantRepository.save(applicant);
    }


    public Optional<ApplicantEntity> get(long id) {
        return applicantRepository.findById(id);
    }


    public List<ApplicantEntity> get(CourseEntity course, boolean isApplied, boolean isRejected) {
        return applicantRepository.findAllByCourseAndIsAppliedAndIsRejected(course, isApplied,
                isRejected);
    }


    public Optional<ApplicantEntity> get(PersonEntity person, CourseEntity course) {
        return applicantRepository.findByPersonAndCourse(person, course);
    }


    public ApplicantEntity create(PersonEntity person, CourseEntity course) {
        Optional<ApplicantEntity> applicant = get(person, course);
        return applicant.isPresent() ? applicant.get()
                : applicantRepository.save(new ApplicantEntity(person, course));
    }


    public ApplicantEntity apply(ApplicantEntity applicant) {
        applicant.setApplied(true);
        this.emailSenderService.sendEmail(applicant.getPerson().getEmail(),
                "Заявку на навчання прийнято!",
                "Вітаємо на курсі \"" + applicant.getCourse().getName() + "\"");
        return applicantRepository.save(applicant);
    }


    public ApplicantEntity reject(ApplicantEntity applicant) {
        applicant.setRejected(true);
        this.emailSenderService.sendEmail(applicant.getPerson().getEmail(),
                "Заявку на навчання відхилено!",
                "Відмова навчання на курсі \"" + applicant.getCourse().getName() + "\"");
        return applicantRepository.save(applicant);
    }

    
    public List<ApplicantEntity> reject(Collection<ApplicantEntity> applicants) {
        return applicantRepository.saveAll(applicants.stream()
                .map(ApplicantEntity::reject).toList());
    }
    
}
