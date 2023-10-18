package com.group.practic.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.group.practic.PropertyLoader;
import com.group.practic.entity.AdditionalMaterialsEntity;
import com.group.practic.entity.ChapterEntity;
import com.group.practic.entity.CourseEntity;
import com.group.practic.entity.LevelEntity;
import com.group.practic.repository.CourseRepository;
import com.group.practic.util.PropertyUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CourseServiceTest {
/*
    @InjectMocks
    private CourseService courseService;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private ChapterService chapterService;

    @Mock
    private LevelService levelService;

    @Mock
    private PropertyLoader propertyLoader;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCourses() {
        List<CourseEntity> courseList = new ArrayList<>();
        when(courseRepository.findAll()).thenReturn(courseList);

        List<CourseEntity> result = courseService.get();

        assertSame(courseList, result);
    }

    @Test
    void testGetCourseById() {
        long courseId = 1;
        CourseEntity course = new CourseEntity();
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        Optional<CourseEntity> result = courseService.get(courseId);

        assertTrue(result.isPresent());
        assertSame(course, result.get());
    }

    @Test
    void testGetCourseBySlug() {
        String slug = "course-slug";
        CourseEntity course = new CourseEntity();
        when(courseRepository.findBySlug(slug)).thenReturn(Optional.of(course));

        Optional<CourseEntity> result = courseService.get(slug);

        assertTrue(result.isPresent());
        assertSame(course, result.get());
    }

    @Test
    void testGetLevels() {
        String slug = "course-slug";
        CourseEntity course = new CourseEntity();
        when(courseRepository.findBySlug(slug)).thenReturn(Optional.of(course));

        List<LevelEntity> levels = new ArrayList<>();
        when(levelService.getAll(course)).thenReturn(levels);

        List<LevelEntity> result = courseService.getLevels(slug);

        assertSame(levels, result);
    }

    @Test
    void testGetChapterByNumber() {
        String slug = "course-slug";
        int number = 1;
        CourseEntity course = new CourseEntity();
        when(courseService.get(slug)).thenReturn(Optional.of(course));

        ChapterEntity chapter = new ChapterEntity(1, course, number, "chapter1", "Chapter 1");
        when(chapterService.getChapterByNumber(course, number)).thenReturn(Optional.of(chapter));

        Optional<ChapterEntity> result = courseService.getChapterByNumber(slug, number);

        assertTrue(result.isPresent());
        assertSame(chapter, result.get());
    }

    @Test
    void testGetPurpose() {
        long courseId = 1;
        CourseEntity course = new CourseEntity();
        course.setPurpose("Test purpose");
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        Optional<String> result = courseService.getPurpose(courseId);

        assertTrue(result.isPresent());
        assertEquals("Test purpose", result.get());
    }

    @Test
    void testGetDescription() {
        long courseId = 1;
        CourseEntity course = new CourseEntity();
        course.setDescription("Test description");
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        Optional<String> result = courseService.getDescription(courseId);

        assertTrue(result.isPresent());
        assertEquals("Test description", result.get());
    }

    @Test
    void testGetAdditional() {
        String slug = "course-slug";
        CourseEntity course = new CourseEntity();
        Set<AdditionalMaterialsEntity> additionalMaterials = new HashSet<>();
        course.setAdditionalMaterials(additionalMaterials);
        when(courseService.get(slug)).thenReturn(Optional.of(course));

        Set<AdditionalMaterialsEntity> result = courseService.getAdditional(slug);

        assertSame(additionalMaterials, result);
    }

    @Test
    void testGetByShortName() {
        String shortName = "courseShortName";
        CourseEntity course = new CourseEntity();
        when(courseRepository.findByShortName(shortName)).thenReturn(Optional.of(course));

        Optional<CourseEntity> result = courseService.getByShortName(shortName);

        assertTrue(result.isPresent());
        assertSame(course, result.get());
    }

    @Test
    void testChangeShortName() {
        long courseId = 1;
        CourseEntity course = new CourseEntity();
        when(courseService.get(courseId)).thenReturn(Optional.of(course));

        String newShortName = "new-short-name";
        Optional<CourseEntity> result = courseService.changeShortName(courseId, newShortName);

        assertTrue(result.isPresent());
        assertEquals(newShortName, result.get().getShortName());
    }

    @Test
    void testSaveCourse() {
        CourseEntity course = new CourseEntity();
        when(courseRepository.save(course)).thenReturn(course);

        Optional<CourseEntity> result = courseService.save(course);

        assertTrue(result.isPresent());
        assertSame(course, result.get());
    }

    @Test
    void testGetAuthorSet() {
        Properties properties = new Properties();
        properties.setProperty(PropertyUtil.AUTHOR_KEY + "1", "Author 1");
        properties.setProperty(PropertyUtil.AUTHOR_KEY + "2", "Author 2");
        properties.setProperty("other.property", "Value");

        when(propertyLoader.getEntrySet()).thenReturn(properties.entrySet());

        String result = courseService.getAuthorSet(propertyLoader);
        Set<String> resultSet = Arrays.stream(result.split(PropertyUtil.AUTHOR_SEPARATOR))
                .collect(Collectors.toSet());

        assertEquals(Set.of("Author 1", "Author 2"), resultSet);
    }
    */
}