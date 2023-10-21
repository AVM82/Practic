package com.group.practic.service;

import com.group.practic.dto.AdditionalMaterialsDto;
import com.group.practic.dto.ChapterDto;
import com.group.practic.dto.SendMessageDto;
import com.group.practic.entity.AdditionalMaterialsEntity;
import com.group.practic.entity.ChapterEntity;
import com.group.practic.entity.CourseEntity;
import com.group.practic.entity.PersonApplicationEntity;
import com.group.practic.entity.PersonEntity;
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


    public Optional<StudentOnCourseEntity> create(CourseEntity course, PersonEntity user) {
        StudentOnCourseEntity student = new StudentOnCourseEntity(user, course);
        Optional<ChapterEntity> firstChapter = courseService.getFirstChapter(course);
        student.setActiveChapter(firstChapter.isPresent() ? firstChapter.get() : null);
        personService.addRolesToPerson(user, PersonService.ROLE_STUDENT);
        PersonApplicationEntity applicant =
                personApplicationRepository.findByPersonAndCourse(user, course);
        applicant.setApply(true);
        personService.save(user);
        personApplicationRepository.save(applicant);
        this.notify(user, course.getSlug());
        return Optional.ofNullable(studentOnCourseRepository.save(student));
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


    public boolean isStudentOfCourse(CourseEntity course) {
        return getStudentOfCourse(course) != null;
    }


    public ChapterEntity getActiveChapter(CourseEntity course) {
        StudentOnCourseEntity studentOnCourse = getStudentOfCourse(course);
        return studentOnCourse != null ? studentOnCourse.getActiveChapter() : null;
    }


    public Integer getActiveChapterNumber(CourseEntity course) {
        ChapterEntity chapter = getActiveChapter(course);
        if (chapter != null) {
            chapter.getNumber();
        }
        return personService.hasAdvancedRole() || personService.amImentor(course)
                ? Integer.MAX_VALUE
                : 0;
    }


    public Optional<ChapterEntity> getOpenedChapter(CourseEntity course, int number) {
        ChapterEntity activeChapter = getActiveChapter(course);
        return (activeChapter != null && activeChapter.getNumber() >= number)
                || personService.amImentor(course)
                        ? chapterService.getChapterByNumber(course, number)
                        : Optional.empty();
    }


    public Optional<Boolean> changeStudentAdditionalMaterial(String slug, long id, boolean state) {
        Optional<CourseEntity> course = courseService.get(slug);
        if (course.isPresent()) {
            StudentOnCourseEntity student = getStudentOfCourse(course.get());
            if (student != null) {
                Optional<AdditionalMaterialsEntity> additionalMaterial =
                        additionalMaterialsService.get(id);
                if (additionalMaterial.isPresent()) {
                    student.changeAdditionalMaterial(additionalMaterial.get(), state);
                    studentOnCourseRepository.save(student);
                    return Optional.of(true);
                }
            }
        }
        return Optional.empty();
    }


    public Integer getLastVisibleChapterNumber(CourseEntity course) {
        if (personService.hasAdvancedRole() || personService.amImentor(course)) {
            return Integer.MAX_VALUE;
        }
        StudentOnCourseEntity studentOnCourse = getStudentOfCourse(course);
        return studentOnCourse == null ? 0 : studentOnCourse.getActiveChapter().getNumber();
    }


    public List<ChapterDto> getChapters(String slug) {
        Optional<CourseEntity> course = courseService.get(slug);
        return course.isEmpty() ? List.of()
                : Converter.convertChapterList(chapterService.getAll(course.get()),
                        getLastVisibleChapterNumber(course.get()));
    }


    public List<AdditionalMaterialsDto> getStudentAdditionalMaterial(String slug) {
        Optional<CourseEntity> course = courseService.get(slug);
        if (course.isPresent()) {
            StudentOnCourseEntity student =
                    studentOnCourseRepository.findByCourseAndStudentAndInactiveAndBan(course.get(),
                            personService.getPerson(), false, false);
            if (student != null) {
                Set<AdditionalMaterialsEntity> studentsAdditional = student.getAdditionalMaterial();
                return course.get().getAdditionalMaterials().stream().map(
                        add -> AdditionalMaterialsDto.map(add, studentsAdditional.contains(add)))
                        .toList();
            }
        }
        return List.of();
    }

}
