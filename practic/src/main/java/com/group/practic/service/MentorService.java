package com.group.practic.service;

import com.group.practic.dto.ApplicantDto;
import com.group.practic.dto.ApplicantsForCourseDto;
import com.group.practic.dto.MentorComplexDto;
import com.group.practic.dto.MentorDto;
import com.group.practic.dto.SendMessageDto;
import com.group.practic.dto.StudentDto;
import com.group.practic.entity.ApplicantEntity;
import com.group.practic.entity.CourseEntity;
import com.group.practic.entity.MentorEntity;
import com.group.practic.entity.PersonEntity;
import com.group.practic.entity.StateMentorEntity;
import com.group.practic.repository.MentorRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class MentorService {

    MentorRepository mentorRepository;

    ApplicantService applicantService;

    StudentService studentService;

    CourseService courseService;

    PersonService personService;

    EmailSenderService emailSenderService;


    @Autowired
    public MentorService(MentorRepository mentorRepository, ApplicantService applicantService,
            StudentService studentService, CourseService courseService, PersonService personService,
            EmailSenderService emailSenderService) {
        this.mentorRepository = mentorRepository;
        this.applicantService = applicantService;
        this.studentService = studentService;
        this.courseService = courseService;
        this.personService = personService;
        this.emailSenderService = emailSenderService;
    }


    public Optional<MentorEntity> get(long id) {
        return mentorRepository.findById(id);
    }


    public List<MentorEntity> get(PersonEntity person) {
        return mentorRepository.findAllByPerson(person);
    }


    public MentorEntity create(PersonEntity person, CourseEntity course) {
        MentorEntity mentor = mentorRepository.findByPersonAndCourse(person, course);
        if (mentor == null) {
            mentor = new MentorEntity(person, course);
        } else {
            mentor.setInactive(false);
        }
        mentor = mentorRepository.save(mentor);
        courseService.addMentor(mentor);
        personService.checkOut(mentor);
        mentorAddNotify(mentor);
        return mentor;
    }


    private void mentorAddNotify(MentorEntity mentor) {
        SendMessageDto messageDto = new SendMessageDto();
        messageDto.setAddress(mentor.getPerson().getEmail());
        messageDto.setHeader("Новий ментор.");
        messageDto.setMessage(
                "Вітаємо Вас, як ментора, на курсі \"" + mentor.getCourse().getName() + "\". ");
        this.emailSenderService.sendMessage(messageDto);
    }


    public MentorComplexDto addMentor(PersonEntity person, CourseEntity course) {
        MentorEntity mentor = create(person, course);
        Optional<StateMentorEntity> stateMentor = person.getState(mentor);
        return stateMentor.isEmpty() ? null
                : new MentorComplexDto(MentorDto.map(mentor), stateMentor.get());
    }


    public boolean removeMentor(MentorEntity mentor) {
        courseService.removeMentor(mentor);
        mentor.setInactive(true);
        personService.checkOut(mentor);
        mentorRemoveNotify(mentor);
        return true;
    }


    private void mentorRemoveNotify(MentorEntity mentor) {
        SendMessageDto messageDto = new SendMessageDto();
        messageDto.setAddress(mentor.getPerson().getEmail());
        messageDto.setHeader("Не ментор.");
        messageDto.setMessage("Вітаємо Вас. Ви вже не ментор на курсі \""
                + mentor.getCourse().getName() + "\". ");
        this.emailSenderService.sendMessage(messageDto);
    }


    public List<ApplicantDto> getApplicantsForCourse(CourseEntity course) {
        List<ApplicantDto> applicants = new ArrayList<>();
        applicantService.get(course, false, false)
                .forEach(applicant -> applicants.add(ApplicantDto.map(applicant)));
        return applicants;
    }


    public List<ApplicantsForCourseDto> getMyApplicants() {
        List<ApplicantsForCourseDto> myApplicants = new ArrayList<>();
        get(PersonService.me()).forEach(
                mentor -> myApplicants.add(new ApplicantsForCourseDto(mentor.getCourse().getSlug(),
                        getApplicantsForCourse(mentor.getCourse()))));
        return myApplicants;
    }


    public Optional<StudentDto> adminStudent(ApplicantEntity applicant) {
        return applicant.isApplied() ? Optional.of(StudentDto.map(applicant.getStudent()))
                : StudentDto.map(studentService.create(applicantService.apply(applicant)));
    }


    public ApplicantEntity rejectApplicant(ApplicantEntity applicant) {
        personService.checkOut(applicantService.reject(applicant));
        return applicant;
    }

}
