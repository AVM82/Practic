package com.group.practic.controller;

import static com.group.practic.util.ResponseUtils.getResponse;
import static com.group.practic.util.ResponseUtils.postResponse;

import com.group.practic.entity.StudentOnCourseEntity;
import com.group.practic.service.StudentOnCourseService;
import jakarta.validation.constraints.Min;
import java.util.Collection;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/student")
public class StudentOnCourseController {

    @Autowired
    StudentOnCourseService studentOnCourseService;


    @GetMapping
    public ResponseEntity<Collection<StudentOnCourseEntity>> get(
            @RequestParam(required = false) Optional<Long> courseId,
            @RequestParam(required = false) Optional<Long> studentId,
            @RequestParam(required = false) boolean inactive,
            @RequestParam(required = false) boolean ban) {
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
        return getResponse(studentOnCourseService.get(id));
    }


    @PostMapping
    public ResponseEntity<StudentOnCourseEntity> create(@Min(1) @RequestParam long courseId,
            @Min(1) @RequestParam long studentId) {
        return postResponse(studentOnCourseService.create(courseId, studentId));
    }

}
