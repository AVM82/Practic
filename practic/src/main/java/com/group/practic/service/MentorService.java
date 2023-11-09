package com.group.practic.service;

import com.group.practic.dto.ApplicantDto;
import com.group.practic.dto.ApplicantsForCourseDto;
import com.group.practic.dto.MentorDto;
import com.group.practic.dto.StudentDto;
import com.group.practic.entity.ApplicantEntity;
import com.group.practic.entity.CourseEntity;
import com.group.practic.entity.MentorEntity;
import com.group.practic.entity.PersonEntity;
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
//        personService.checkOut(mentor);
        this.emailSenderService.sendEmail(mentor.getPerson().getEmail(), "Новий ментор.", 
                "Вітаємо Вас як ментора курсу \"" + mentor.getCourse().getName() + "\". ");
        return mentor;
    }


    public MentorDto addMentor(PersonEntity person, CourseEntity course) {
        MentorEntity mentor = create(person, course);
//        Optional<StateMentorEntity> stateMentor = person.getMentorStates().stream()
//                .filter(state -> state.getMentorId() == mentor.getId()).findAny();
        return MentorDto.map(mentor);
    }


    public boolean removeMentor(MentorEntity mentor) {
        courseService.removeMentor(mentor);
        mentor.setInactive(true);
//        personService.checkOut(mentor);
        this.emailSenderService.sendEmail(mentor.getPerson().getEmail(), "Не ментор.", 
                "Вітаємо Вас. Ви вже не ментор курса \"" + mentor.getCourse().getName() + "\". ");
        return true;
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
                mentor -> myApplicants.add(new ApplicantsForCourseDto(mentor.getCourse().getName(),
                        getApplicantsForCourse(mentor.getCourse()))));
        return myApplicants;
    }


    public Optional<StudentDto> adminStudent(ApplicantEntity applicant) {
        return applicant.isApplied() ? Optional.of(StudentDto.map(applicant.getStudent()))
                : StudentDto.map(studentService.create(applicantService.apply(applicant)));
    }


    public ApplicantEntity rejectApplicant(ApplicantEntity applicant) {
        return applicantService.reject(applicant);
    }

}
