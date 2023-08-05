package com.group.practic.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.group.practic.dto.CourseDTO;
import com.group.practic.entity.Course;
import com.group.practic.service.CourseService;

import java.util.List;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        return ResponseEntity.ok(courseService.findAll());
    }

    @PostMapping
    public ResponseEntity<Course> createCourse(@RequestBody CourseDTO courseDTO) {
        return new ResponseEntity<>(courseService.save(courseDTO), HttpStatus.CREATED);
    }

    @PostMapping("/{courseName}")
    public ResponseEntity<Course> addStudentToCourse(@PathVariable String courseName,
                                                     @RequestParam String studentPib) {
        Course course = courseService.addStudentToCourse(courseName, studentPib);

        if (course == null) {
            return ResponseEntity.notFound().build();
        }

        return new ResponseEntity<>(course, HttpStatus.CREATED);
    }
}
