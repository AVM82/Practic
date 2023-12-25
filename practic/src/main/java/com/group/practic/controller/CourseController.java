package com.group.practic.controller;

import static com.group.practic.util.ResponseUtils.getResponse;
import static com.group.practic.util.ResponseUtils.postResponse;

import com.group.practic.dto.ChapterCompleteDto;
import com.group.practic.dto.ChapterDto;
import com.group.practic.dto.CourseDto;
import com.group.practic.dto.NewCourseDto;
import com.group.practic.entity.AdditionalMaterialsEntity;
import com.group.practic.entity.LevelEntity;
import com.group.practic.service.CourseService;
import com.group.practic.service.PersonService;
import jakarta.validation.constraints.NotBlank;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private CourseService courseService;


    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }


    @GetMapping
    public ResponseEntity<Collection<CourseDto>> get() {
        return getResponse(courseService.get().stream().map(CourseDto::map).toList());
    }


    @GetMapping("/{slug}/allchapters")
    public ResponseEntity<Collection<ChapterDto>> getChapters(@PathVariable String slug) {
        return getResponse(courseService.get(slug).map(
                course -> courseService.getChapters(course, !PersonService.hasAdvancedRole())));
    }


    @GetMapping("/{slug}/levels")
    public ResponseEntity<Collection<LevelEntity>> getLevels(@PathVariable String slug) {
        return getResponse(courseService.getLevels(slug));
    }


    @GetMapping("/{slug}/additional")
    public ResponseEntity<Collection<AdditionalMaterialsEntity>> getAdditional(
            @PathVariable String slug) {
        return getResponse(courseService.getAdditional(slug));
    }


    @PostMapping("/NewCourseFromProperties")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<CourseDto> createCourse(@NotBlank @RequestBody String propertyFile) {
        return postResponse(courseService.create(propertyFile).map(CourseDto::map));
    }


    @PostMapping("/NewCourse")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<CourseDto> createCourse(@NotBlank @RequestBody NewCourseDto dto) {
        return postResponse(courseService.create(dto).map(CourseDto::map));
    }


    @GetMapping("/{slug}")
    public ResponseEntity<CourseDto> getBySlug(@PathVariable String slug) {
        return getResponse(courseService.get(slug).map(CourseDto::map));
    }


    @GetMapping("/{slug}/chapters/{number}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'COMRADE')")
    public ResponseEntity<ChapterCompleteDto> getChapterByNumber(@PathVariable("slug") String slug,
            @PathVariable int number) {
        return getResponse(courseService.get(slug).map(course -> courseService
                .getChapterByNumber(course, number).map(ChapterCompleteDto::map).orElse(null)));
    }

}
