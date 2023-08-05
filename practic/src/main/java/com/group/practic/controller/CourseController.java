package com.group.practic.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.group.practic.dto.CourseDTO;
import com.group.practic.entity.CourseEntity;
import com.group.practic.service.CourseService;

import java.util.List;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping
    public ResponseEntity<List<CourseEntity>> getAllCourses() {
        return ResponseEntity.ok(courseService.findAll());
    }

    @PostMapping
    public ResponseEntity<CourseEntity> createCourse(@RequestBody CourseDTO courseDTO) {
        return new ResponseEntity<>(courseService.save(courseDTO), HttpStatus.CREATED);
    }

    @PostMapping("/{courseName}")
    public ResponseEntity<CourseEntity> addStudentToCourse(@PathVariable String courseName,
                                                           @RequestParam String studentPib) {
        CourseEntity courseEntity = courseService.addStudentToCourse(courseName, studentPib);

        if (courseEntity == null) {
            return ResponseEntity.notFound().build();
        }

        return new ResponseEntity<>(courseEntity, HttpStatus.CREATED);
    }
}
