package com.group.practic.service;

import com.group.practic.dto.ChapterDto;
import com.group.practic.dto.SendMessageDto;
import com.group.practic.entity.AdditionalMaterialsEntity;
import com.group.practic.entity.ChapterEntity;
import com.group.practic.entity.CourseEntity;
import com.group.practic.entity.PersonApplicationEntity;
import com.group.practic.entity.PersonEntity;
import com.group.practic.entity.RoleEntity;
import com.group.practic.entity.StudentOnCourseEntity;
import com.group.practic.repository.PersonApplicationRepository;
import com.group.practic.repository.RoleRepository;
import com.group.practic.repository.StudentOnCourseRepository;
import com.group.practic.util.Converter;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class StudentOnCourseService {

    StudentOnCourseRepository studentOnCourseRepository;

    CourseService courseService;

    ChapterService chapterService;

    AdditionalMaterialsService additionalMaterialsService;

    PersonService personService;

    RoleRepository roleRepository;

    PersonApplicationRepository personApplicationRepository;

    EmailSenderService emailSenderService;


    @Autowired
    public StudentOnCourseService(StudentOnCourseRepository studentOnCourseRepository,
            CourseService courseService, PersonService personService, RoleRepository roleRepository,
            PersonApplicationRepository personApplicationRepository, ChapterService chapterService,
            EmailSenderService emailSenderService,
            AdditionalMaterialsService additionalMaterialsService) {
        super();
        this.studentOnCourseRepository = studentOnCourseRepository;
        this.courseService = courseService;
        this.chapterService = chapterService;
        this.additionalMaterialsService = additionalMaterialsService;
        this.personService = personService;
        this.roleRepository = roleRepository;
        this.personApplicationRepository = personApplicationRepository;
        this.emailSenderService = emailSenderService;
    }


    public List<StudentOnCourseEntity> get() {
        return studentOnCourseRepository.findAll();
    }


    public StudentOnCourseEntity get(long id) {
        return studentOnCourseRepository.findByStudentId(id);
    }


    public List<StudentOnCourseEntity> get(boolean inactive, boolean ban) {
        return studentOnCourseRepository.findAllByInactiveAndBan(inactive, ban);
    }


    public StudentOnCourseEntity get(long courseId, long studentId, boolean inactive, boolean ban) {
        Optional<CourseEntity> course = courseService.get(courseId);
        Optional<PersonEntity> student = personService.get(studentId);
        return (course.isPresent() && student.isPresent())
                ? studentOnCourseRepository.findByCourseAndStudentAndInactiveAndBan(course.get(),
                        student.get(), inactive, ban)
                : null;
    }


    public List<StudentOnCourseEntity> getCoursesOfStudent(long studentId, boolean inactive,
            boolean ban) {
        Optional<PersonEntity> student = personService.get(studentId);
        return student.isPresent()
                ? studentOnCourseRepository.findAllByStudentAndInactiveAndBan(student.get(),
                        inactive, ban)
                : List.of();
    }


    public List<StudentOnCourseEntity> getStudentsOfCourse(long courseId, boolean inactive,
            boolean ban) {
        Optional<CourseEntity> course = courseService.get(courseId);
        return course.isPresent()
                ? studentOnCourseRepository.findAllByCourseAndInactiveAndBan(course.get(), inactive,
                        ban)
                : List.of();
    }


    public Optional<StudentOnCourseEntity> create(long courseId, long studentId) {
        Optional<CourseEntity> course = courseService.get(courseId);
        Optional<PersonEntity> user = personService.get(studentId);

        Optional<StudentOnCourseEntity> student = (course.isPresent() && user.isPresent())
                ? Optional.of(studentOnCourseRepository
                        .save(new StudentOnCourseEntity(user.get(), course.get())))
                : Optional.empty();
        if (student.isPresent()) {
            PersonEntity updateUser = user.get();
            PersonApplicationEntity applicant =
                    personApplicationRepository.findByPersonAndCourse(updateUser, course.get());
            applicant.setApply(true);
            Set<RoleEntity> roles = updateUser.getRoles();
            roles.add(roleRepository.findByName("STUDENT"));
            roles.add(roleRepository.findByName(course.get().getSlug()));
            updateUser.setRoles(roles);
            personService.save(updateUser);
            personApplicationRepository.save(applicant);
            this.notify(user.get(), course.get().getSlug());
        }

        return student;
    }


    private void notify(PersonEntity student, String slug) {
        SendMessageDto messageDto = new SendMessageDto();
        messageDto.setAddress(student.getEmail());
        messageDto.setHeader("Заявку на навчання на курсі " + slug + " прийнято!");
        messageDto.setMessage("Вітаємо на курсі " + slug);
        this.emailSenderService.sendMessage(messageDto);
    }


    public StudentOnCourseEntity getStudentOfCourse(CourseEntity course) {
        PersonEntity person = personService.getPerson();
        return person == null ? null
                : studentOnCourseRepository.findByCourseAndStudentAndInactiveAndBan(course, person,
                        false, false);
    }


    public boolean isStudentofCourse(CourseEntity course) {
        return getStudentOfCourse(course) != null;
    }


    public ChapterEntity getActiveChapter(CourseEntity course) {
        StudentOnCourseEntity studentOnCourse = getStudentOfCourse(course);
        return studentOnCourse != null ? studentOnCourse.getActiveChapter() : null;
    }


    public Integer getActiveChapterNumber(CourseEntity course) {
        StudentOnCourseEntity studentOnCourse = getStudentOfCourse(course);
        return studentOnCourse != null ? studentOnCourse.getActiveChapter().getNumber() : 0;
    }


    public Optional<ChapterEntity> getOpenedChapter(CourseEntity course, int number) {
        ChapterEntity activeChapter = getActiveChapter(course);
        return (activeChapter != null && activeChapter.getNumber() >= number)
                || personService.amIMentor(course)
                        ? chapterService.getChapterByNumber(course, number)
                        : Optional.empty();
    }


    public Optional<Boolean> changeStudentAdditionalMaterial(StudentOnCourseEntity student, long id,
            boolean state) {
        Optional<AdditionalMaterialsEntity> additionalMaterial = additionalMaterialsService.get(id);
        if (additionalMaterial.isPresent()) {
            student.changeAdditionalMaterial(additionalMaterial.get(), state);
            studentOnCourseRepository.save(student);
            return Optional.of(true);
        }
        return Optional.empty();
    }


    public int getLastVisibleChapterNumber(CourseEntity course) {
        if (personService.hasAdvancedRole() || personService.amIMentor(course))
            return Integer.MAX_VALUE;
        StudentOnCourseEntity studentOnCourse = getStudentOfCourse(course);
        return studentOnCourse == null ? 0 : studentOnCourse.getActiveChapter().getNumber();
    }


    public List<ChapterDto> getChapters(String slug) {
        Optional<CourseEntity> course = courseService.get(slug);
        return course.isEmpty() ? List.of()
                : Converter.convertChapterEntityList(chapterService.getAll(course.get()),
                        getLastVisibleChapterNumber(course.get()));
    }

}
