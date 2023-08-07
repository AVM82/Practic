package com.group.practic.controller;

import com.group.practic.dto.StudentDto;
import com.group.practic.entity.StudentEntity;
import com.group.practic.service.CourseService;
import com.group.practic.service.StudentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<StudentEntity> createCourse(@RequestBody StudentDto studentDto) {
        return new ResponseEntity<>(studentService.save(studentDto), HttpStatus.CREATED);
    }
}
