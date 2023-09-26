package com.group.practic.controller;

import static com.group.practic.util.ResponseUtils.getResponse;
import static com.group.practic.util.ResponseUtils.postResponse;

import com.group.practic.dto.ChapterDto;
import com.group.practic.entity.AdditionalMaterialsEntity;
import com.group.practic.entity.ChapterEntity;
import com.group.practic.entity.CourseEntity;
import com.group.practic.entity.LevelEntity;
import com.group.practic.entity.PersonEntity;
import com.group.practic.entity.StudentChapterEntity;
import com.group.practic.service.CourseService;
import com.group.practic.service.StudentChapterService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
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

    private StudentChapterService studentChapterService;

    @Autowired
    public CourseController(CourseService courseService,
            StudentChapterService studentChapterService) {
        this.courseService = courseService;
        this.studentChapterService = studentChapterService;
    }


    @GetMapping
    public ResponseEntity<Collection<CourseEntity>> get() {
        return getResponse(courseService.get());
    }


    @GetMapping("/{slug}/allchapters")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Collection<ChapterDto>> getChapters(@PathVariable String slug) {
        return getResponse(courseService.getChapters(slug));
    }


    @GetMapping("/{slug}/levels")
    public ResponseEntity<Collection<LevelEntity>> getLevels(@PathVariable String slug) {
        return getResponse(courseService.getLevels(slug));
    }


    @GetMapping("/{id}/purpose")
    public ResponseEntity<String> getPurpose(@Min(1) @PathVariable long id) {
        return getResponse(courseService.getPurpose(id));
    }


    @GetMapping("/{id}/description")
    public ResponseEntity<String> getDescription(@Min(1) @PathVariable long id) {
        return getResponse(courseService.getDescription(id));
    }


    @GetMapping("/{slug}/additional")
    public ResponseEntity<Collection<AdditionalMaterialsEntity>> getAdditional(
            @PathVariable String slug) {
        return getResponse(courseService.getAdditional(slug));
    }


    @PostMapping("/NewCourseFromProperties")
    public ResponseEntity<CourseEntity> createCourse(@Valid @RequestBody String propertyFile) {
        return postResponse(courseService.create(propertyFile));
    }


    @PostMapping
    public ResponseEntity<CourseEntity> createCourse(
            @NotBlank @RequestBody CourseEntity courseEntity) {
        return postResponse(courseService.save(courseEntity));
    }


    @GetMapping("/{slug}")
    public ResponseEntity<CourseEntity> getBySlug(@PathVariable String slug) {
        return getResponse(courseService.get(slug));
    }


    @GetMapping("/{slug}/chapters/{number}")
    @PreAuthorize("hasRole(#slug)||hasRole('ADMIN')")
    public ResponseEntity<ChapterEntity> getChapterByNumber(@PathVariable("slug") String slug,
            @PathVariable int number) {
        Optional<ChapterEntity> chapter = courseService.getChapterByNumber(slug, number);
        if (chapter.isPresent() && isChapterOpen(chapter.get())) {
            return getResponse(courseService.getChapterByNumber(slug, number));
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

    }


    private boolean isChapterOpen(ChapterEntity chapter) {
        PersonEntity person = (PersonEntity) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        if (person != null) {
            Set<StudentChapterEntity> studentChapters =
                    studentChapterService.findOpenChapters(person);

            return studentChapters.stream().anyMatch(
                    studentChapter -> studentChapter.getChapter().getId() == chapter.getId());

        } else {
            return false;
        }
    }

}