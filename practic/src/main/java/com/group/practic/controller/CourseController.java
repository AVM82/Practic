package com.group.practic.controller;

import static com.group.practic.util.ResponseUtils.getResponse;
import static com.group.practic.util.ResponseUtils.getResponseAllowed;
import static com.group.practic.util.ResponseUtils.notAcceptable;
import static com.group.practic.util.ResponseUtils.postResponse;

import com.group.practic.dto.ChapterDto;
import com.group.practic.dto.CourseDto;
import com.group.practic.entity.AdditionalMaterialsEntity;
import com.group.practic.entity.ChapterEntity;
import com.group.practic.entity.CourseEntity;
import com.group.practic.entity.LevelEntity;
import com.group.practic.entity.PersonEntity;
import com.group.practic.service.CourseService;
import com.group.practic.service.PersonService;
import com.group.practic.service.StudentOnCourseService;
import com.group.practic.util.Converter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    private PersonService personService;

    private StudentOnCourseService studentOnCourseService;

    static final List<String> ACCESS_ROLES = List.of(PersonService.ROLE_ADMIN,
            PersonService.ROLE_COLLABORATOR, PersonService.ROLE_MENTOR);


    @Autowired
    public CourseController(CourseService courseService, PersonService personService,
            StudentOnCourseService studentOnCourseService) {
        this.courseService = courseService;
        this.personService = personService;
        this.studentOnCourseService = studentOnCourseService;
    }


    @GetMapping
    public ResponseEntity<Collection<CourseDto>> get() {
        return getResponse(Converter.convertCourseEntityList(courseService.get()));
    }


    @GetMapping("/{slug}/allchapters")
    public ResponseEntity<Collection<ChapterDto>> getChapters(@PathVariable String slug) {
        return getResponse(personService.hasAdvancedRole() ? courseService.getChapters(slug)
                : studentOnCourseService.getChapters(slug));
    }


    @GetMapping("/{slug}/levels")
    public ResponseEntity<Collection<LevelEntity>> getLevels(@PathVariable String slug) {
        return getResponse(courseService.getLevels(slug));
    }


    @GetMapping("/{slug}/description")
    public ResponseEntity<String> getDescription(@Min(1) @PathVariable String slug) {
        return getResponse(courseService.getDescription(slug));
    }


    @GetMapping("/{slug}/additionalExist")
    public ResponseEntity<Boolean> getAdditionalExist(@PathVariable String slug) {
        return getResponse(courseService.getAdditionalExist(slug));
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
    public ResponseEntity<ChapterEntity> getChapterByNumber(@PathVariable("slug") String slug,
            @PathVariable int number) {
        if (personService.hasAnyRole(ACCESS_ROLES)) {
            return getResponseAllowed(courseService.getChapterByNumber(slug, number));
        }
        Optional<CourseEntity> course = courseService.get(slug);
        if (course.isEmpty()) { return notAcceptable(); }
        return getResponseAllowed(studentOnCourseService.getOpenedChapter(course.get(), number));
    }


    @GetMapping("/{slug}/mentors")
    public ResponseEntity<Collection<PersonEntity>> getMentors(@PathVariable String slug) {
        return getResponse(courseService.getMentors(slug));
    }

}
