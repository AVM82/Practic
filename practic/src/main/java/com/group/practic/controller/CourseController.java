package com.group.practic.controller;

import com.group.practic.dto.CourseDto;
import com.group.practic.entity.CourseEntity;
import com.group.practic.service.CourseService;
import com.group.practic.util.ResponseUtils;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/course")
public class CourseController {

  @Autowired
  private CourseService courseService;


  @GetMapping
  public ResponseEntity<Collection<CourseEntity>> get() {
    return ResponseUtils.getResponse(courseService.get());
  }


  @GetMapping("/{id}")
  public ResponseEntity<CourseEntity> get(@PathVariable long id) {
    return ResponseUtils.getResponse(courseService.get(id));
  }


  @PostMapping
  public ResponseEntity<CourseEntity> createCourse(@RequestBody CourseDto courseDto) {
    return ResponseUtils.postResponse(courseService.create(courseDto));
  }


}
