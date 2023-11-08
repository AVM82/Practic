package com.group.practic.service;

import static com.group.practic.util.Converter.nonNullList;

import com.group.practic.dto.AdditionalMaterialsDto;
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
import java.time.LocalDate;
import java.util.ArrayList;
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
            StudentChapterEntity studentChapter = studentChapterRepository.save(
                    new StudentChapterEntity(chapter.get()));
            student.addChapter(studentChapter);
            this.emailSenderService.sendEmail(student.getPerson().getEmail(),
                    "Новий розділ відкрито.",
                    "Розділ №" + studentChapter.getNumber() + " " + 
                            studentChapter.getChapter().getShortName());
        }
        return studentRepository.save(student);
    }


    public Optional<StudentEntity> create(ApplicantEntity applicant) {
        if (get(applicant.getPerson(), applicant.getCourse()).isEmpty()) {
            StudentEntity student = openNextChapter(
                    studentRepository.save(new StudentEntity(applicant)));
            personService.checkOut(student);
            return Optional.of(student);
        }
        return Optional.empty();
    }


    void finish(StudentEntity student) {
        student.setInactive(true);
 //       student.setActiveChapterNumber(null);
        student.setFinish(LocalDate.now());
        // student.setWeeks(student.getFinish() - student.getStart());
        // finishNotify(student);
    }


    public Optional<StudentEntity> getStudentOfCourse(CourseEntity course) {
        return studentRepository.findByPersonAndCourseAndInactiveAndBan(PersonService.me(), course,
                false, false);
    }


    public List<ShortChapterDto> getChapters(StudentEntity student) {
        int visibleMaxNumber = student.getActiveChapterNumber();
        List<ShortChapterDto> result = new ArrayList<>();
        result.addAll(nonNullList(student.getStudentChapters()).stream()
                .map(ShortChapterDto::map)
                .toList());
        if (visibleMaxNumber > 0)
            result.addAll(nonNullList(student.getCourse().getChapters()).stream()
                    .filter(chapter -> chapter.getNumber() > visibleMaxNumber)
                    .map(chapter -> ShortChapterDto.map(chapter, true)).toList());
        return result;
    }


    public Optional<StudentChapterEntity> getOpenedChapter(StudentEntity student, int number) {
        return student.getStudentChapters().stream()
                        .filter(chapter -> chapter.getChapter().getNumber() == number).findAny();
    }


    public Optional<Boolean> changeAdditionalMaterial(StudentEntity student,
            AdditionalMaterialsEntity additionalMaterial, boolean state) {
        return Optional.of(studentRepository.save(
                student.changeAdditionalMaterial(additionalMaterial, state)) != null);
    }


    public List<AdditionalMaterialsDto> getAdditionalMaterials(StudentEntity student) {
        Set<AdditionalMaterialsEntity> studentAdd = student.getAdditionalMaterial();
        return student.getCourse().getAdditionalMaterials().stream()
                .map(add -> AdditionalMaterialsDto.map(add, studentAdd.contains(add)))
                .toList();
    }

}
