package com.group.practic.service;

import static com.group.practic.util.Converter.nonNullList;

import com.group.practic.dto.AdditionalMaterialsDto;
import com.group.practic.dto.SendMessageDto;
import com.group.practic.dto.ShortChapterDto;
import com.group.practic.entity.AdditionalMaterialsEntity;
import com.group.practic.entity.ApplicantEntity;
import com.group.practic.entity.ChapterEntity;
import com.group.practic.entity.CourseEntity;
import com.group.practic.entity.PersonEntity;
import com.group.practic.entity.StudentChapterEntity;
import com.group.practic.entity.StudentEntity;
import com.group.practic.repository.StudentChapterRepository;
import com.group.practic.repository.StudentRepository;
import com.group.practic.util.Converter;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class StudentService {

    StudentRepository studentRepository;

    CourseService courseService;

    StudentChapterRepository studentChapterRepository;

    PersonService personService;

    EmailSenderService emailSenderService;


    @Autowired
    public StudentService(StudentRepository studentRepository, CourseService courseService,
            PersonService personService, ChapterService chapterService,
            StudentChapterRepository studentChapterRepository,
            EmailSenderService emailSenderService) {
        super();
        this.studentRepository = studentRepository;
        this.courseService = courseService;
        this.studentChapterRepository = studentChapterRepository;
        this.personService = personService;
        this.emailSenderService = emailSenderService;
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


    public Optional<StudentEntity> get(PersonEntity person, CourseEntity course, boolean inactive, boolean ban) {
        return studentRepository.findByPersonAndCourseAndInactiveAndBan(person, course, inactive, ban);
    }


    public List<StudentEntity> getCoursesOfPerson(PersonEntity person, boolean inactive, boolean ban) {
        return studentRepository.findAllByPersonAndInactiveAndBan(person, inactive, ban);
    }
    
    
    public List<StudentEntity> getStudentsOfCourse(long courseId, boolean inactive, boolean ban) {
        Optional<CourseEntity> course = courseService.get(courseId);
        return course.isPresent()
                ? studentRepository.findAllByCourseAndInactiveAndBan(course.get(), inactive, ban)
                : List.of();
    }


    public StudentEntity openNextChapter(StudentEntity student) {
        Optional<ChapterEntity> chapter = courseService.getNextChapterByNumber(student.getCourse(),
                student.getActiveChapterNumber());
        if (chapter.isEmpty()) {
            finish(student);
        } else {
            student.addChapter(studentChapterRepository
                    .save(new StudentChapterEntity(student, chapter.get())));
            nextChapterNotify(student);
        }
        return studentRepository.save(student);
    }


    public Optional<StudentEntity> create(ApplicantEntity applicant) {
        CourseEntity course = applicant.getCourse();
        if (get(applicant.getPerson(), course).isEmpty()) {
            StudentEntity student = openNextChapter(studentRepository.save(new StudentEntity(applicant)));
            personService.checkOut(student);
            applyNotify(student);
            return Optional.of(student);
        }
        return Optional.empty();
    }


    void finish(StudentEntity student) {
        student.setInactive(true);
        student.setActiveChapter(null);
        student.setFinish(LocalDate.now());
        // student.setWeeks(student.getFinish() - student.getStart());
        // finishNotify(student);
    }


    private void nextChapterNotify(StudentEntity student) {
        SendMessageDto messageDto = new SendMessageDto();
        messageDto.setAddress(student.getPerson().getEmail());
        messageDto.setHeader("Новий розділ відкрито.");
        messageDto.setMessage("Розділ №" + student.getActiveChapterNumber() + " "
                + student.getActiveChapter().getChapter().getShortName());
        this.emailSenderService.sendMessage(messageDto);
    }


    private void applyNotify(StudentEntity student) {
        SendMessageDto messageDto = new SendMessageDto();
        messageDto.setAddress(student.getPerson().getEmail());
        messageDto.setHeader("Заявку на навчання прийнято!");
        messageDto.setMessage("Вітаємо на курсі \"" + student.getCourse().getName() + "\"");
        this.emailSenderService.sendMessage(messageDto);
    }


    public Optional<StudentEntity> getStudentOfCourse(CourseEntity course) {
        return studentRepository.findByPersonAndCourseAndInactiveAndBan(PersonService.me(), course,
                false, false);
    }


    public List<ShortChapterDto> getChapters(CourseEntity course) {
        Optional<StudentEntity> student = getStudentOfCourse(course);
        if (student.isPresent()) {
            Set<ChapterEntity> chapters = course.getChapters();
            Set<StudentChapterEntity> studentChapters = student.get().getChapters();
            return Converter.convertChapterList(nonNullList(studentChapters), nonNullList(chapters),
                    student.get().getActiveChapterNumber());
        }
        return List.of();
    }


    public Optional<StudentChapterEntity> getOpenedChapter(CourseEntity course, int number) {
        Optional<StudentEntity> student = getStudentOfCourse(course);
        return student.isEmpty() ? Optional.empty()
                : student.get().getChapters().stream()
                        .filter(chapter -> chapter.getChapter().getNumber() == number).findAny();
    }


    public Optional<Boolean> changeStudentAdditionalMaterial(StudentEntity student,
            AdditionalMaterialsEntity additionalMaterial, boolean state) {
        studentRepository.save(student.changeAdditionalMaterial(additionalMaterial, state));
        return Optional.of(true);
    }


    public List<AdditionalMaterialsDto> getStudentAdditionalMaterial(CourseEntity course) {
        Optional<StudentEntity> student = getStudentOfCourse(course);
        if (student.isPresent()) {
            Set<AdditionalMaterialsEntity> studentsAdd = student.get().getAdditionalMaterial();
            return course.getAdditionalMaterials().stream()
                    .map(add -> AdditionalMaterialsDto.map(add, studentsAdd.contains(add)))
                    .toList();
        }
        return List.of();
    }

}
