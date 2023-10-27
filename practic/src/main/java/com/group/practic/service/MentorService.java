package com.group.practic.service;

import com.group.practic.dto.SendMessageDto;
import com.group.practic.entity.ApplicantEntity;
import com.group.practic.entity.CourseEntity;
import com.group.practic.entity.MentorEntity;
import com.group.practic.entity.PersonEntity;
import com.group.practic.entity.StudentEntity;
import com.group.practic.repository.MentorRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class MentorService {

    MentorRepository mentorRepository;

    ApplicantService applicantService;

    StudentService studentService;

    CourseService courseService;

    EmailSenderService emailSenderService;


    @Autowired
    public MentorService(MentorRepository mentorRepository, ApplicantService applicantService,
            StudentService studentService, CourseService courseService,
            EmailSenderService emailSenderService) {
        this.mentorRepository = mentorRepository;
        this.applicantService = applicantService;
        this.studentService = studentService;
        this.courseService = courseService;
        this.emailSenderService = emailSenderService;
    }


    public Optional<MentorEntity> get(long id) {
        return mentorRepository.findById(id);
    }


    public MentorEntity create(PersonEntity person, CourseEntity course, String linkedInUrl) {
        return mentorRepository.save(new MentorEntity(person, course, linkedInUrl));
    }


    public MentorEntity update(MentorEntity mentor) {
        return mentorRepository.save(mentor);
    }


    public boolean adminStudent(ApplicantEntity applicant) {
        Optional<StudentEntity> student = studentService.create(applicant);
        return student.isPresent();
    }


    public boolean rejectApplicant(ApplicantEntity applicant) {
        boolean success = applicantService.reject(applicant);
        if (success) {
            rejectNotify(applicant);
        }
        return success;
    }


    private void rejectNotify(ApplicantEntity applicant) {
        SendMessageDto messageDto = new SendMessageDto();
        messageDto.setAddress(applicant.getPerson().getEmail());
        messageDto.setHeader("Заявку на навчання відхилено!");
        messageDto.setMessage(
                "Відмова навчання на курсі \"" + applicant.getCourse().getName() + "\"");
        this.emailSenderService.sendMessage(messageDto);
    }

}
