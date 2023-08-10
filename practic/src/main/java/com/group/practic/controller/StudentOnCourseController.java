package com.group.practic.controller;

import static com.group.practic.util.ResponseUtils.getResponse;
import static com.group.practic.util.ResponseUtils.postResponse;
import com.group.practic.entity.StudentOnCourseEntity;
import com.group.practic.service.StudentOnCourseService;
import java.util.Collection;
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
      @RequestParam(required = false) long courseId,
      @RequestParam(required = false) long studentId,
      @RequestParam(required = false) boolean inactive,
      @RequestParam(required = false) boolean ban) {
    if (courseId == 0) {
      if (studentId == 0)
        return getResponse(studentOnCourseService.get(inactive, ban));
      return getResponse(studentOnCourseService.getCoursesOfStudent(studentId, inactive, ban));
    }
    if (studentId == 0)
      return getResponse(studentOnCourseService.getStudentsOfCourse(courseId, inactive, ban));
    return getResponse(studentOnCourseService.get(courseId, studentId, inactive, ban));
  }


  @GetMapping("/{id}")
  public ResponseEntity<StudentOnCourseEntity> get(@PathVariable long id) {
    return getResponse(studentOnCourseService.get(id));
  }


  @PostMapping
  public ResponseEntity<StudentOnCourseEntity> create(@RequestParam long courseId, 
      @RequestParam long studentId) {
    return postResponse(studentOnCourseService.create(studentId, courseId));
  }

  
}
