package com.group.practic.controller;

import static com.group.practic.util.ResponseUtils.getResponse;
import static com.group.practic.util.ResponseUtils.postResponse;

import com.group.practic.dto.ChapterDto;
import com.group.practic.dto.NewStudentDto;
import com.group.practic.dto.StudentChapterDto;
import com.group.practic.dto.NewStudentReportDto;
import com.group.practic.dto.StudentPracticeDto;
import com.group.practic.dto.StudentReportDto;
import com.group.practic.entity.CourseEntity;
import com.group.practic.entity.StudentChapterEntity;
import com.group.practic.entity.PersonEntity;
import com.group.practic.entity.StudentOnCourseEntity;
import com.group.practic.entity.StudentPracticeEntity;
import com.group.practic.entity.StudentReportEntity;
import com.group.practic.enumeration.PracticeState;
import com.group.practic.enumeration.ReportState;
import com.group.practic.exception.ResourceNotFoundException;
import com.group.practic.service.CourseService;
import com.group.practic.service.PersonService;
import com.group.practic.service.StudentChapterService;
import com.group.practic.service.StudentOnCourseService;
import com.group.practic.service.StudentPracticeService;
import com.group.practic.service.StudentReportService;
import com.group.practic.util.Converter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.security.Principal;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/students")
public class StudentOnCourseController {

    private final StudentOnCourseService studentOnCourseService;

    private final StudentPracticeService studentPracticeService;

    private final PersonService personService;

    private final StudentReportService studentReportService;

    private final CourseService courseService;

    private final StudentChapterService studentChapterService;


    @Autowired
    public StudentOnCourseController(StudentOnCourseService studentOnCourseService,
                                     StudentPracticeService studentPracticeService,
                                     PersonService personService,
                                     StudentReportService studentReportService,
                                     CourseService courseService,
                                     StudentChapterService studentChapterService) {
        this.studentOnCourseService = studentOnCourseService;
        this.studentPracticeService = studentPracticeService;
        this.personService = personService;
        this.studentReportService = studentReportService;
        this.courseService = courseService;
        this.studentChapterService = studentChapterService;
    }


    @GetMapping
    public ResponseEntity<Collection<StudentOnCourseEntity>> get(
            @RequestParam(required = false) Optional<Long> courseId,
            @RequestParam(required = false) Optional<Long> studentId,
            @RequestParam(required = false) boolean inactive,
            @RequestParam(required = false) boolean ban) {
        if (!personService.isCurrentPersonMentor()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if (courseId.isEmpty()) {
            if (studentId.isEmpty()) {
                return getResponse(studentOnCourseService.get(inactive, ban));
            }
            return getResponse(
                    studentOnCourseService.getCoursesOfStudent(studentId.get(), inactive, ban));
        }
        if (studentId.isEmpty()) {
            return getResponse(
                    studentOnCourseService.getStudentsOfCourse(courseId.get(), inactive, ban));
        }
        return getResponse(
                studentOnCourseService.get(courseId.get(), studentId.get(), inactive, ban));
    }


    @GetMapping("/{id}")
    public ResponseEntity<StudentOnCourseEntity> get(@Min(1) @PathVariable long id) {
        return personService.isCurrentPersonMentor()
                ? ResponseEntity.ok(studentOnCourseService.get(id))
                : new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }


    @PostMapping
    @PreAuthorize("hasRole('ADMIN')||hasRole('MENTOR')")
    public ResponseEntity<StudentOnCourseEntity> create(@RequestBody @Valid
                                                        NewStudentDto student) {
        Optional<CourseEntity> courseOptional  = courseService.get(student.getCourseSlug());

        if (courseOptional.isPresent()) {
            return postResponse(
                    studentOnCourseService.create(
                            courseOptional.get().getId(),
                            student.getUserId())
            );
        } else {
            throw new ResourceNotFoundException("Курс ", student.getCourseSlug(), " не знайдено");
        }
    }


    @GetMapping("/practices/{practiceState}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Collection<StudentPracticeDto>> getPracticeWithStateFilter(
            @PathVariable String practiceState) {
        PracticeState state = PracticeState.fromString(practiceState);
        List<StudentPracticeEntity> students = studentPracticeService.getAllStudentsByState(state);
        return getResponse(students.stream().map(Converter::convert).toList());
    }


    @GetMapping("/practices/states")
    public ResponseEntity<Collection<String>> getPracticeStates() {
        List<String> practiceStates = Arrays.stream(PracticeState.values())
                .map(state -> state.name().toLowerCase()).toList();
        return getResponse(practiceStates);
    }

    @GetMapping("/reports/states")
    public ResponseEntity<Collection<String>> getReportStates() {
        List<String> reportStates = Arrays.stream(ReportState.values())
                .map(state -> state.name().toLowerCase()).toList();
        return getResponse(reportStates);
    }

    @GetMapping("/reports/course/{slug}")
    public ResponseEntity<Collection<List<StudentReportDto>>> getActualStudentReports(
            @PathVariable String slug) {

        return getResponse(Converter.convertListOfLists(
            studentReportService.getAllStudentsActualReports(slug)));

    }

    @GetMapping("/chapters")
    public ResponseEntity<Set<ChapterDto>> getOpenChapters() {
        PersonEntity person = (PersonEntity) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        Set<StudentChapterEntity> studentOpenChapters =
                studentChapterService.findOpenChapters(person);
        return ResponseEntity.ok(studentOpenChapters.stream()
                .map(Converter::convert).collect(Collectors.toSet()));
    }

    @PostMapping("/chapters")
    public ResponseEntity<StudentChapterEntity> createStudentChapter(
            @RequestBody @Valid StudentChapterDto student) {

        return ResponseEntity.ok(
                studentChapterService.addChapter(student.getStudentId(), student.getChapterId())
        );
    }

    @PostMapping("/practices")
    public ResponseEntity<StudentPracticeDto> setPracticeState(
            @RequestBody StudentPracticeDto studentPracticeDto
    ) {
        PersonEntity person = (PersonEntity) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        PracticeState practiceState = PracticeState.fromString(studentPracticeDto.getState());
        StudentPracticeEntity studentPractice =
                studentPracticeService.getPractice(person, studentPracticeDto.getChapterPartId());
        studentPractice.setState(practiceState);
        return ResponseEntity.ok(
                Converter.convert(studentPracticeService.save(studentPractice))
        );

    }


    @PostMapping("/reports/course/{slug}")
    public ResponseEntity<StudentReportDto> postStudentReport(Principal principal, @RequestBody
            NewStudentReportDto newStudentReportDto) {

        Optional<PersonEntity> personEntity = personService.get(principal.getName());
        Optional<StudentReportEntity> reportEntity =
                studentReportService.createStudentReport(personEntity, newStudentReportDto);
        return postResponse(Optional.ofNullable(Converter.convert(reportEntity.get())));
    }
}
