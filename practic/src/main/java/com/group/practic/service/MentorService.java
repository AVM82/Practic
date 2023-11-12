package com.group.practic.service;

import com.group.practic.dto.ApplicantDto;
import com.group.practic.dto.ApplicantsForCourseDto;
import com.group.practic.dto.ChapterDto;
import com.group.practic.dto.MentorDto;
import com.group.practic.dto.ShortChapterDto;
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

    StudentReportService reportService;

    EmailSenderService emailSenderService;


    @Autowired
    public MentorService(MentorRepository mentorRepository, ApplicantService applicantService,
            StudentService studentService, CourseService courseService, PersonService personService,
            EmailSenderService emailSenderService, StudentReportService reportService) {
        this.mentorRepository = mentorRepository;
        this.applicantService = applicantService;
        this.studentService = studentService;
        this.courseService = courseService;
        this.personService = personService;
        this.emailSenderService = emailSenderService;
        this.reportService = reportService;
    }


    public Optional<MentorEntity> get(long id) {
        return mentorRepository.findById(id);
    }


    public List<MentorEntity> get(PersonEntity person) {
        return mentorRepository.findAllByPerson(person);
    }


    public boolean isMyCourse(CourseEntity course) {
        return mentorRepository.findByPersonAndCourse(PersonService.me(), course) != null;
    }


    public MentorEntity create(PersonEntity person, CourseEntity course) {
        MentorEntity mentor = mentorRepository.findByPersonAndCourse(person, course);
        if (mentor == null) {
            mentor = new MentorEntity(person, course);
        } else {
            mentor.setInactive(false);
        }
        mentor = mentorRepository.save(mentor);
        personService.addMentor(mentor);
        courseService.addMentor(mentor);
        this.emailSenderService.sendEmail(mentor.getPerson().getEmail(), "Новий ментор.",
                "Вітаємо Вас як ментора курсу \"" + mentor.getCourse().getName() + "\". ");
        return mentor;
    }


    public MentorDto addMentor(PersonEntity person, CourseEntity course) {
        MentorEntity mentor = create(person, course);
        return MentorDto.map(mentor);
    }


    public boolean removeMentor(MentorEntity mentor) {
        courseService.removeMentor(mentor);
        personService.removeMentor(mentor);
        this.emailSenderService.sendEmail(mentor.getPerson().getEmail(), "Не ментор.",
                "Вітаємо Вас. Ви вже не ментор курса \"" + mentor.getCourse().getName() + "\". ");
        mentorRepository.delete(mentor);
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


    public StudentDto adminStudent(ApplicantEntity applicant) {
        return applicant.isApplied() ? StudentDto.map(applicant.getStudent())
                : StudentDto.map(studentService.create(applicantService.apply(applicant)));
    }


    public ApplicantEntity rejectApplicant(ApplicantEntity applicant) {
        return applicant.isApplied() ? applicant
                : applicantService.reject(applicant);
    }


    public List<ShortChapterDto> getChapters(CourseEntity course) {
        return Optional.ofNullable(course.getChapters())
                        .map(chapters -> chapters.stream()
                                .map(chapter -> ShortChapterDto.map(chapter,
                                        reportService.getActualReportCount(chapter)))
                                .toList())
                        .orElseGet(List::of);
    }


    public Optional<ChapterDto> getChapter(CourseEntity course, int number) {
        return courseService.getChapterByNumber(course, number).map(
                chapter -> ChapterDto.map(chapter, reportService.getActualReportCount(chapter)));
    }

}
