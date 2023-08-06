package com.group.practic.controller;

import com.group.practic.dto.CourseDto;
import com.group.practic.entity.CourseEntity;
import com.group.practic.service.CourseService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<CourseEntity> createCourse(@RequestBody CourseDto courseDto) {
        return new ResponseEntity<>(courseService.save(courseDto), HttpStatus.CREATED);
    }

    @PostMapping("/{courseName}")
    public ResponseEntity<CourseEntity> addStudentToCourse(
            @PathVariable String courseName, @RequestParam String studentPib) {
        CourseEntity courseEntity = courseService.addStudentToCourse(courseName, studentPib);

        if (courseEntity == null) {
            return ResponseEntity.notFound().build();
        }

        return new ResponseEntity<>(courseEntity, HttpStatus.CREATED);
    }
}
