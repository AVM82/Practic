package com.group.practic.service;

import static com.group.practic.util.Converter.nonNullList;

import com.group.practic.dto.AdditionalMaterialsDto;
import com.group.practic.dto.NewStateChapterDto;
import com.group.practic.dto.ShortChapterDto;
import com.group.practic.dto.StudentChapterDto;
import com.group.practic.entity.AdditionalMaterialsEntity;
import com.group.practic.entity.ApplicantEntity;
import com.group.practic.entity.ChapterEntity;
import com.group.practic.entity.CourseEntity;
import com.group.practic.entity.DaysCountable;
import com.group.practic.entity.PersonEntity;
import com.group.practic.entity.StudentChapterEntity;
import com.group.practic.entity.StudentEntity;
import com.group.practic.enumeration.ChapterState;
import com.group.practic.repository.StudentChapterRepository;
import com.group.practic.repository.StudentRepository;
import com.group.practic.util.TimeCalculator;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class StudentService {

    StudentRepository studentRepository;

    StudentChapterRepository studentChapterRepository;

    PersonService personService;

    CourseService courseService;

    StudentReportService reportService;

    EmailSenderService emailSenderService;


    @Autowired
    public StudentService(StudentRepository studentRepository, CourseService courseService,
            PersonService personService, ChapterService chapterService,
            StudentChapterRepository studentChapterRepository,
            EmailSenderService emailSenderService, StudentReportService reportService) {
        this.studentRepository = studentRepository;
        this.courseService = courseService;
        this.studentChapterRepository = studentChapterRepository;
        this.personService = personService;
        this.emailSenderService = emailSenderService;
        this.reportService = reportService;
    }


    public List<StudentEntity> get() {
        return studentRepository.findAll();
    }


    public Optional<StudentEntity> get(long id) {
        return studentRepository.findById(id);
    }


    public List<StudentEntity> get(boolean inactive, boolean ban) {
        return studentRepository.findAllByInactiveAndBan(inactive, ban);
    }


    public Optional<StudentEntity> get(PersonEntity person, CourseEntity course) {
        return studentRepository.findByPersonAndCourse(person, course);
    }


    public Optional<StudentEntity> get(PersonEntity person, CourseEntity course, boolean inactive,
            boolean ban) {
        return studentRepository.findByPersonAndCourseAndInactiveAndBan(person, course, inactive,
                ban);
    }


    public List<StudentEntity> getCoursesOfPerson(PersonEntity person, boolean inactive,
            boolean ban) {
        return studentRepository.findAllByPersonAndInactiveAndBan(person, inactive, ban);
    }


    public List<StudentEntity> getStudentsOfCourse(long courseId, boolean inactive, boolean ban) {
        Optional<CourseEntity> course = courseService.get(courseId);
        return course.isPresent()
                ? studentRepository.findAllByCourseAndInactiveAndBan(course.get(), inactive, ban)
                : List.of();
    }


    public Optional<StudentChapterEntity> getStudentChapter(long studentChapterId) {
        return studentChapterRepository.findById(studentChapterId);
    }



    public StudentEntity create(PersonEntity person, CourseEntity course) {
        if (get(person, course).isEmpty()) {
            StudentEntity student = studentRepository.save(new StudentEntity(person, course));
            personService.addStudent(person);
            return openNextChapter(student);
        }
        return null;
    }


    public StudentEntity create(ApplicantEntity applicant) {
        return create(applicant.getPerson(), applicant.getCourse());
    }


    protected StudentEntity openNextChapter(StudentEntity student) {
        Optional<ChapterEntity> chapter = courseService.getNextChapterByNumber(student.getCourse(),
                student.getActiveChapterNumber());
        return studentRepository
                .save(chapter.isPresent() ? nextChapter(student, chapter.get()) : finish(student));
    }


    protected StudentEntity nextChapter(StudentEntity student, ChapterEntity chapter) {
        StudentChapterEntity studentChapter =
                studentChapterRepository.save(new StudentChapterEntity(student, chapter));
        student.addChapter(studentChapter);
        this.emailSenderService.sendEmail(student.getPerson().getEmail(), "Новий розділ відкрито.",
                "Розділ №" + studentChapter.getNumber() + " "
                        + studentChapter.getChapter().getShortName());
        return student;
    }


    protected StudentEntity start(StudentEntity student) {
        student.setStart(LocalDate.now());
        return studentRepository.save(student);
    }


    protected StudentEntity finish(StudentEntity student) {
        student.setInactive(true);
        student.setActiveChapterNumber(0);
        student.setFinish(LocalDate.now());
        student.setWeeks(TimeCalculator.daysToWeeksWithRounding(
                Period.between(student.getFinish(), student.getStart()).getDays() + 1));
        student.setDaysSpent(student.getStudentChapters().stream().map(DaysCountable::getDaysSpent)
                .reduce(0, (a, b) -> a + b));
        this.emailSenderService.sendEmail(student.getPerson().getEmail(), "Курс завершено",
                "Вітаємо ви закінчили навчання на курсі \"" + student.getCourse().getName() + "\"");
        return studentRepository.save(student);
    }


    public Optional<StudentEntity> getStudentOfCourse(CourseEntity course) {
        return studentRepository.findByPersonAndCourseAndInactiveAndBan(PersonService.me(), course,
                false, false);
    }


    public List<ShortChapterDto> getChapters(StudentEntity student) {
        int visibleMaxNumber = student.getActiveChapterNumber();
        List<ShortChapterDto> result = new ArrayList<>();
        result.addAll(
                nonNullList(student.getStudentChapters()).stream()
                        .map(chapter -> ShortChapterDto.map(chapter,
                                reportService.getActualReportCount(chapter.getChapter())))
                        .toList());
        if (visibleMaxNumber > 0) {
            result.addAll(nonNullList(student.getCourse().getChapters()).stream()
                    .filter(chapter -> chapter.getNumber() > visibleMaxNumber)
                    .map(chapter -> ShortChapterDto.map(chapter, true)).toList());
        }
        return result;
    }


    public Optional<StudentChapterDto> getOpenedChapter(StudentEntity student, int number) {
        return student.getStudentChapters().stream()
                .filter(chapter -> chapter.getChapter().getNumber() == number).findAny()
                .map(StudentChapterDto::map);
    }


    protected boolean allowToCloseChapter(StudentChapterEntity chapter) {
        // --> complete the test immediately
        return chapter.getState().changeAllowed(ChapterState.DONE);
        // && chapter.getPractices().stream()
        // .filter(practice -> practice.getState()== PracticeState.APPROVED).count()
        // == chapter.getChapter().getParts().size()
        // && chapter.getReportCount() > 0
        // && chapter.quizResultIsSatisfactory
    }


    protected StudentChapterEntity changeChapterState(StudentChapterEntity chapter,
            ChapterState newState) {
        // --> complete the test immediately
        if (chapter.getState() == ChapterState.NOT_STARTED && newState == ChapterState.IN_PROCESS
                && chapter.getNumber() == 1) {
            start(chapter.getStudent());
        }
        if ((newState != ChapterState.DONE || allowToCloseChapter(chapter))
                && TimeCalculator.setNewState(chapter, newState)) {
            studentChapterRepository.save(chapter);
            if (newState == ChapterState.DONE) {
                openNextChapter(chapter.getStudent());
            }
        }
        return chapter;
    }


    public Optional<NewStateChapterDto> changeState(StudentChapterEntity chapter,
            ChapterState newState) {
        return Optional.of(new NewStateChapterDto(changeChapterState(chapter, newState)));
    }


    public Optional<Boolean> changeAdditionalMaterial(StudentEntity student,
            AdditionalMaterialsEntity additionalMaterial, boolean state) {
        return Optional.of(studentRepository
                .save(student.changeAdditionalMaterial(additionalMaterial, state)) != null);
    }


    public List<AdditionalMaterialsDto> getAdditionalMaterials(StudentEntity student) {
        List<AdditionalMaterialsEntity> studentAdd = student.getAdditionalMaterials();
        return student.getCourse().getAdditionalMaterials().stream()
                .map(add -> AdditionalMaterialsDto.map(add, studentAdd.contains(add))).toList();
    }

}
