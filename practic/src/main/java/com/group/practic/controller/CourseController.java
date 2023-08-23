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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;


    @GetMapping
    public ResponseEntity<Collection<CourseEntity>> get() {
        return getResponse(courseService.get());
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


    @PutMapping("/{id}/change/shortNname")
    public ResponseEntity<CourseEntity> addShortName(@PathVariable long id,
            @RequestParam String shortName) {
        return postResponse(courseService.addShortName(id, shortName));
    }


    @GetMapping("/{slug}")
    public ResponseEntity<CourseEntity> getBySlug(@PathVariable String slug) {
        return getResponse(courseService.getBySlug(slug));
    }


    @GetMapping("/{slug}/chapters/{number}")
    public ResponseEntity<ChapterEntity> getChapterByNumber(@PathVariable String slug,
            @PathVariable int number) {
        return getResponse(courseService.getChapterByNumber(slug, number));
    }

}
