package com.group.practic.controller;

import static com.group.practic.util.ResponseUtils.getResponse;
import static com.group.practic.util.ResponseUtils.postResponse;

import com.group.practic.dto.CourseDto;
import com.group.practic.entity.ChapterEntity;
import com.group.practic.entity.CourseEntity;
import com.group.practic.service.CourseService;
import jakarta.validation.constraints.Min;
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
@RequestMapping("/api/course")
public class CourseController {

    @Autowired
    private CourseService courseService;


    @GetMapping
    public ResponseEntity<Collection<CourseEntity>> get() {
        return getResponse(courseService.get());
    }


    @GetMapping("/{id}")
    public ResponseEntity<CourseEntity> get(@Min(1) @PathVariable long id) {
        return getResponse(courseService.get(id));
    }


    @GetMapping("/{id}/chapters")
    public ResponseEntity<Collection<ChapterEntity>> getChapters(@Min(1) @PathVariable long id) {
        return getResponse(courseService.getChapters(id));
    }


    @GetMapping("/{id}/purpose")
    public ResponseEntity<String> getPurpose(@Min(1) @PathVariable long id) {
        return getResponse(courseService.getPurpose(id));
    }


    @GetMapping("/{id}/description")
    public ResponseEntity<String> getDescription(@Min(1) @PathVariable long id) {
        return getResponse(courseService.getDescription(id));
    }


    @GetMapping("/{id}/additional")
    public ResponseEntity<ChapterEntity> getAdditional(@Min(1) @PathVariable long id) {
        return getResponse(courseService.getAdditional(id));
    }


    @PostMapping
    public ResponseEntity<CourseEntity> createCourse(@RequestBody CourseDto courseDto) {
        return postResponse(courseService.create(courseDto));
    }


}
