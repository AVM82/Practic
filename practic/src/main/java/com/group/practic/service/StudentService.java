package com.group.practic.service;

import static com.group.practic.util.Converter.nonNullList;

import com.group.practic.dto.AdditionalMaterialsDto;
import com.group.practic.dto.ChapterCompleteDto;
import com.group.practic.dto.ChapterDto;
import com.group.practic.dto.NewStateChapterDto;
import com.group.practic.dto.PracticeDto;
import com.group.practic.entity.AdditionalMaterialsEntity;
import com.group.practic.entity.ApplicantEntity;
import com.group.practic.entity.ChapterEntity;
import com.group.practic.entity.CourseEntity;
import com.group.practic.entity.DaysCountable;
import com.group.practic.entity.PersonEntity;
import com.group.practic.entity.ReportEntity;
import com.group.practic.entity.StudentChapterEntity;
import com.group.practic.entity.StudentEntity;
import com.group.practic.entity.StudentPracticeEntity;
import com.group.practic.entity.SubChapterEntity;
import com.group.practic.enumeration.ChapterState;
import com.group.practic.enumeration.PracticeState;
import com.group.practic.repository.StudentChapterRepository;
import com.group.practic.repository.StudentPracticeRepository;
import com.group.practic.repository.StudentRepository;
import com.group.practic.util.TimeCalculator;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentService {

    public static final long MIN_REPORT_COUNT_PER_CHAPTER = 1;

    private final StudentRepository studentRepository;

    private final StudentChapterRepository studentChapterRepository;

    private final StudentPracticeRepository studentPracticeRepository;

    private final PersonService personService;

    private final CourseService courseService;

    private final ReportService reportService;

    private final EmailSenderService emailSenderService;


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

    public List<StudentEntity> getStudentsOfCourse(CourseEntity course, boolean inactive,
                                                   boolean ban) {
        return studentRepository.findAllByCourseAndInactiveAndBanOrderByActiveChapterNumber(course,
                inactive, ban);
    }

    public Optional<StudentChapterEntity> getStudentChapter(long studentChapterId) {
        return studentChapterRepository.findById(studentChapterId);
    }

    public StudentEntity create(PersonEntity person, CourseEntity course) {
        if (get(person, course).isEmpty()) {
            StudentEntity student = studentRepository.save(new StudentEntity(person, course));
            personService.addStudentRole(person);
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
        student.setActiveChapterNumber(chapter.getNumber());
        createPractices(studentChapter);
        this.emailSenderService.sendEmail(student.getPerson().getEmail(), "Новий розділ відкрито.",
                "Розділ №" + chapter.getNumber() + " " + chapter.getShortName());
        return student;
    }


    protected void closeChapter(StudentChapterEntity studentChapter) {
        StudentEntity student = studentChapter.getStudent();
        ChapterEntity chapter = studentChapter.getChapter();
        Set<String> studentSkills = student.getSkills();
        Set<Long> subs = studentChapter.getSubs();
        chapter.getParts()
                .forEach(part -> part.getSubChapters().stream()
                        .filter(sub -> subs.contains(sub.getId())).map(SubChapterEntity::getSkills)
                        .forEach(studentSkills::addAll));
        student.getSkills().addAll(chapter.getSkills());
        // save ?
        // pass chapter to the statistics
    }


    protected StudentEntity start(StudentEntity student) {
        student.setStart(LocalDate.now());
        return studentRepository.save(student);
    }


    protected StudentEntity finish(StudentEntity student) {
        student.setActiveChapterNumber(0);
        student.setFinish(LocalDate.now());
        student.setWeeks((int) student.getStart().until(student.getFinish(), ChronoUnit.WEEKS));
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

    public List<ChapterDto> getChapters(StudentEntity student) {
        int visibleMaxNumber = student.getActiveChapterNumber();
        List<ReportEntity> allReports =
                reportService.getActual(student.getCourse(), LocalDate.now());
        List<ChapterDto> result = new ArrayList<>();
        result.addAll(ChapterDto.map(student.getStudentChapters(), allReports));
        if (visibleMaxNumber > 0) {
            result.addAll(nonNullList(student.getCourse().getChapters()).stream()
                    .filter(chapter -> chapter.getNumber() > visibleMaxNumber)
                    .map(chapter -> ChapterDto.map(chapter, true, allReports)).toList());
        }
        return result;
    }


    public Optional<ChapterCompleteDto> getOpenedChapter(StudentChapterEntity chapter) {
        return Optional.of(ChapterCompleteDto.map(chapter, reportService.getChapterActual(
                chapter.getStudent().getCourse(), LocalDate.now(), chapter.getNumber())));
    }


    public Optional<ChapterCompleteDto> getOpenedChapter(StudentEntity student, int number) {
        return student.getStudentChapters().stream()
                .filter(chapter -> chapter.getChapter().getNumber() == number).findFirst()
                .map(chapter -> ChapterCompleteDto.map(chapter, reportService
                        .getChapterActual(student.getCourse(), LocalDate.now(), number)));
    }


    protected boolean allowToCloseChapter(StudentChapterEntity chapter) {
        // --> complete the test immediately
        return chapter.getState().changeAllowed(ChapterState.DONE)
                && chapter.getPractices().stream()
                .filter(practice -> practice.getState() == PracticeState.APPROVED)
                .count() == chapter.getChapter().getParts().size()
                && chapter.countApprovedReports() >= MIN_REPORT_COUNT_PER_CHAPTER
                && chapter.isQuizPassed();
    }


    protected StudentChapterEntity changeChapterState(StudentChapterEntity chapter,
                                                      ChapterState newState) {
        if (chapter.getState() == ChapterState.NOT_STARTED && newState == ChapterState.IN_PROCESS
                && chapter.getNumber() == 1) {
            start(chapter.getStudent());
        }
        if ((newState != ChapterState.DONE || allowToCloseChapter(chapter))
                && TimeCalculator.setNewState(chapter, newState)) {
            if (newState == ChapterState.PAUSE) {
                pausePractices(chapter.getPractices());
            }
            studentChapterRepository.save(chapter);
            if (newState == ChapterState.DONE) {
                closeChapter(chapter);
                openNextChapter(chapter.getStudent());
            }
        }
        return chapter;
    }


    public Optional<NewStateChapterDto> changeState(StudentChapterEntity chapter,
                                                    ChapterState newState) {
        return Optional.of(NewStateChapterDto.map(changeChapterState(chapter, newState)));
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


    public Optional<StudentPracticeEntity> getPractice(long id) {
        return studentPracticeRepository.findById(id);
    }


    protected List<StudentPracticeEntity> createPractices(StudentChapterEntity studentChapter) {
        List<StudentPracticeEntity> result = new ArrayList<>();
        studentChapter.getChapter().getParts().forEach(part -> result.add(studentPracticeRepository
                .save(new StudentPracticeEntity(studentChapter, part.getNumber()))));
        return result;
    }


    protected void pausePractices(List<StudentPracticeEntity> practices) {
        practices.forEach(practice -> {
            if (practice.getState() == PracticeState.READY_TO_REVIEW) {
                changePracticeState(practice, PracticeState.IN_PROCESS);
            }
            changePracticeState(practice, PracticeState.PAUSE);
        });
    }


    public PracticeDto changePracticeState(StudentPracticeEntity practice, PracticeState newState) {
        return PracticeDto.map(TimeCalculator.setNewState(practice, newState)
                ? studentPracticeRepository.save(practice)
                : practice);
    }


    public StudentEntity ban(StudentEntity student) {
        student.setBan(true);
        this.emailSenderService.sendEmail(student.getPerson().getEmail(), "Бан на курсі !",
                "Вітаємо. Вас забанено на курсі \"" + student.getCourse().getName() + "\".");
        return studentRepository.save(student);
    }


    public List<StudentEntity> ban(Collection<StudentEntity> students) {
        return students.stream().map(this::ban).toList();
    }


    public boolean reSetSkills(StudentChapterEntity chapter, long subChapterId, boolean state) {
        if (chapter.getChapter().getParts().stream().anyMatch(part -> part.getSubChapters().stream()
                .anyMatch(sub -> sub.getId() == subChapterId))) {
            studentChapterRepository.save(chapter.reSetSub(state, subChapterId));
            return state;
        }
        return false;
    }


    public boolean isCorrectStudentChapter(StudentChapterEntity studentChapter, CourseEntity course,
                                           PersonEntity person) {
        StudentEntity student = studentChapter.getStudent();
        return student.getCourse().equals(course) && student.getPerson().equals(person);
    }


    public void quizPassed(StudentChapterEntity studentChapter) {
        studentChapter.setQuizPassed(true);
        studentChapterRepository.save(studentChapter);
    }

}
