package com.group.practic.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.group.practic.PropertyLoader;
import com.group.practic.dto.CourseDto;
import com.group.practic.entity.AdditionalMaterialsEntity;
import com.group.practic.entity.ChapterEntity;
import com.group.practic.entity.CourseEntity;
import com.group.practic.entity.LevelEntity;
import com.group.practic.entity.PersonEntity;
import com.group.practic.repository.CourseRepository;
import com.group.practic.util.PropertyUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@Slf4j
class CourseServiceTest {

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
    @Mock
    private AdditionalMaterialsService additionalMaterialsService;

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
    void testGetChapters() {
        CourseEntity course = new CourseEntity();
        course.setId(1);

        ChapterEntity chapter1 = new ChapterEntity();
        chapter1.setId(1);
        chapter1.setCourse(course);
        chapter1.setName("Chapter 1");

        ChapterEntity chapter2 = new ChapterEntity();
        chapter2.setId(2);
        chapter2.setCourse(course);
        chapter2.setName("Chapter 2");
        List<ChapterEntity> chapters = new ArrayList<>();
        chapters.add(chapter1);
        chapters.add(chapter2);
        String slug = "example-slug";
        when(courseRepository.findBySlug(slug)).thenReturn(Optional.of(course));
        when(chapterService.getAll(course)).thenReturn(chapters);

        List<ChapterEntity> result = courseService.getChapters(slug);

        assertEquals(2, result.size());
        assertEquals(chapter1, result.get(0));
        assertEquals(chapter2, result.get(1));
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
    void testGetDescription() {
        String slug = "example-slug";
        CourseEntity course = new CourseEntity();
        course.setId(1);
        course.setDescription("Course Description");

        when(courseRepository.findBySlug(slug)).thenReturn(Optional.of(course));

        Optional<String> result = courseService.getDescription(slug);

        assertEquals("Course Description", result.orElse(null));
    }

    @Test
    void testGetDescriptionCourseNotFound() {
        String slug = "non-existent-slug";

        when(courseRepository.findBySlug(slug)).thenReturn(Optional.empty());

        Optional<String> result = courseService.getDescription(slug);

        assertEquals(Optional.empty(), result);
    }

    @Test
    void testGetAdditionalExistWithAdditionalMaterials() {
        CourseEntity course = new CourseEntity();
        course.setId(1);

        Set<AdditionalMaterialsEntity> additionalMaterials = new HashSet<>();
        AdditionalMaterialsEntity material = new AdditionalMaterialsEntity();
        material.setId(1);
        additionalMaterials.add(material);
        course.setAdditionalMaterials(additionalMaterials);
        String slug = "example-slug";
        when(courseRepository.findBySlug(slug)).thenReturn(Optional.of(course));

        Boolean result = courseService.getAdditionalExist(slug);

        assertEquals(true, result);
    }

    @Test
    void testGetAdditionalExistWithoutAdditionalMaterials() {
        String slug = "example-slug";
        CourseEntity course = new CourseEntity();
        course.setId(1);

        Set<AdditionalMaterialsEntity> additionalMaterials = new HashSet<>();
        course.setAdditionalMaterials(additionalMaterials);

        when(courseRepository.findBySlug(slug)).thenReturn(Optional.of(course));

        Boolean result = courseService.getAdditionalExist(slug);

        assertEquals(false, result);
    }

    @Test
    void testGetAdditionalExistCourseNotFound() {
        String slug = "non-existent-slug";

        when(courseRepository.findBySlug(slug)).thenReturn(Optional.empty());

        Boolean result = courseService.getAdditionalExist(slug);

        assertEquals(false, result);
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
    void testSaveCourse() {
        CourseEntity course = new CourseEntity();
        when(courseRepository.save(course)).thenReturn(course);

        Optional<CourseEntity> result = courseService.save(course);

        assertTrue(result.isPresent());
        assertSame(course, result.get());
    }

    @Test
    void testCreateCourseFromDto() {
        CourseDto courseDto = new CourseDto();
        courseDto.setSlug("example-slug");
        courseDto.setName("Example Course");

        CourseEntity createdCourse = new CourseEntity();
        createdCourse.setId(1);
        createdCourse.setSlug(courseDto.getSlug());
        createdCourse.setName(courseDto.getName());

        when(courseRepository.save(any(CourseEntity.class))).thenReturn(createdCourse);

        Optional<CourseEntity> result = courseService.create(courseDto);

        assertEquals(true, result.isPresent());
        assertEquals(1, result.get().getId());
        assertEquals(courseDto.getSlug(), result.get().getSlug());
        assertEquals(courseDto.getName(), result.get().getName());
    }

    @Test
    void testCreateCourseFromDtoWithInvalidData() {
        CourseDto courseDto = new CourseDto();

        Optional<CourseEntity> result = courseService.create(courseDto);

        assertEquals(false, result.isPresent());
    }

    @Test
    void testGetAuthorSet() {
        Properties properties = new Properties();
        properties.setProperty(PropertyUtil.AUTHOR_KEY + "1", "Author 1");
        properties.setProperty(PropertyUtil.AUTHOR_KEY + "2", "Author 2");
        properties.setProperty("other.property", "Value");

        when(propertyLoader.getEntrySet()).thenReturn(properties.entrySet());

        Set<String> resultSet = courseService.getAuthorSet(propertyLoader);

        Set<String> expectedSet = new HashSet<>(Arrays.asList("Author 1", "Author 2"));

        assertEquals(expectedSet, resultSet);
    }

    @Test
    void testGetMentorsForExistingCourse() {
        CourseEntity course = new CourseEntity("example-slug", "Example Course", "example.svg");
        PersonEntity mentor1 = new PersonEntity("Mentor 1", "linkedin.com/mentor1");
        PersonEntity mentor2 = new PersonEntity("Mentor 2", "linkedin.com/mentor2");

        course.getMentors().add(mentor1);
        course.getMentors().add(mentor2);

        when(courseRepository.findBySlug("example-slug")).thenReturn(Optional.of(course));

        Set<PersonEntity> mentors = courseService.getMentors("example-slug");

        assertFalse(mentors.isEmpty());
        assertEquals(2, mentors.size());
        assertTrue(mentors.contains(mentor1));
        assertTrue(mentors.contains(mentor2));
    }
}