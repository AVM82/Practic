package com.group.practic.controller;

import static com.group.practic.util.ResponseUtils.getResponse;
import static com.group.practic.util.ResponseUtils.postResponse;

import com.group.practic.dto.StudentPracticeDto;
import com.group.practic.dto.StudentReportDto;
import com.group.practic.entity.StudentOnCourseEntity;
import com.group.practic.entity.StudentPracticeEntity;
import com.group.practic.enumeration.PracticeState;
import com.group.practic.enumeration.ReportState;
import com.group.practic.service.PersonService;
import com.group.practic.service.StudentOnCourseService;
import com.group.practic.service.StudentPracticeService;
import com.group.practic.service.StudentReportService;
import com.group.practic.util.Converter;
import jakarta.validation.constraints.Min;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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


    @Autowired
    public StudentOnCourseController(StudentOnCourseService studentOnCourseService,
            StudentPracticeService studentPracticeService, PersonService personService,
            StudentReportService studentReportService) {
        this.studentOnCourseService = studentOnCourseService;
        this.studentPracticeService = studentPracticeService;
        this.personService = personService;
        this.studentReportService = studentReportService;
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
        if (!personService.isCurrentPersonMentor()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return getResponse(studentOnCourseService.get(id));
    }


    @PostMapping
    public ResponseEntity<StudentOnCourseEntity> create(@Min(1) @RequestParam long courseId,
            @Min(1) @RequestParam long studentId) {
        if (!personService.isCurrentPersonMentor()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return postResponse(studentOnCourseService.create(courseId, studentId));
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
    public ResponseEntity<Collection<List<StudentReportDto>>> getReportsWithStateAndChapterFilter(
            @PathVariable String slug) {
        return getResponse(Converter
                .convertListOfLists(studentReportService.getAllStudentsActualReports(slug)));
    }

}
