package com.group.practic.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.group.practic.dto.StudentDTO;
import com.group.practic.entity.StudentEntity;
import com.group.practic.service.CourseService;
import com.group.practic.service.StudentService;

import java.util.List;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;
    private final CourseService courseService;

    @GetMapping
    public ResponseEntity<List<StudentEntity>> findAllStudentsByCourseName(
            @RequestParam(required = false) String courseName,
            @RequestParam(required = false, defaultValue = "false") Boolean inactive,
            @RequestParam(required = false, defaultValue = "false") Boolean ban) {

        if (courseName != null) {
            List<StudentEntity> allStudentsByCourseName =
                    courseService.findAllStudentsByCourseName(courseName, inactive, ban);
            return ResponseEntity.ok(allStudentsByCourseName);
        }

        return ResponseEntity.ok(studentService.findAll());
    }

    @PostMapping
    public ResponseEntity<StudentEntity> createCourse(@RequestBody StudentDTO studentDTO) {
        return new ResponseEntity<>(studentService.save(studentDTO), HttpStatus.CREATED);
    }
}
