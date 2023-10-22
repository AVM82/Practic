package com.group.practic.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.group.practic.dto.PersonApplyOnCourseDto;
import com.group.practic.entity.CourseEntity;
import com.group.practic.entity.PersonApplicationEntity;
import com.group.practic.entity.PersonEntity;
import com.group.practic.exception.ResourceNotFoundException;
import com.group.practic.repository.CourseRepository;
import com.group.practic.repository.PersonApplicationRepository;
import com.group.practic.repository.PersonRepository;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class PersonApplicationServiceTest {

    @InjectMocks
    private PersonApplicationService personApplicationService;

    @Mock
    private PersonApplicationRepository personApplicationRepository;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private CourseRepository courseRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testAddPersonApplication() {
        PersonEntity person = new PersonEntity();
        CourseEntity course = new CourseEntity();
        String courseSlug = "sample-course-slug";
        person.setCourses(new HashSet<>());

        when(courseRepository.findBySlug(courseSlug)).thenReturn(Optional.of(course));

        when(personRepository.save(person)).thenReturn(person);

        PersonEntity result = personApplicationService.addPersonApplication(person, courseSlug);

        assertTrue(result.getCourses().contains(course));
    }

    @Test
    void testAddPersonApplicationWithNonExistingCourse() {
        PersonEntity person = new PersonEntity();
        String courseSlug = "non-existing-course";

        when(courseRepository.findBySlug(courseSlug)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> personApplicationService.addPersonApplication(person, courseSlug));
    }

    @Test
    void testGetNotApplyPerson() {
        PersonApplicationEntity application1 = new PersonApplicationEntity();
        PersonEntity person1 = new PersonEntity();
        person1.setName("John Doe");
        application1.setPerson(person1);
        CourseEntity course1 = new CourseEntity();
        course1.setSlug("course-slug-1");
        application1.setCourse(course1);
        application1.setId(1L);

        PersonApplicationEntity application2 = new PersonApplicationEntity();
        PersonEntity person2 = new PersonEntity();
        person2.setName("Jane Smith");
        application2.setPerson(person2);
        CourseEntity course2 = new CourseEntity();
        course2.setSlug("course-slug-2");
        application2.setCourse(course2);
        application2.setId(2L);

        List<PersonApplicationEntity> applications = Arrays.asList(application1, application2);

        when(personApplicationRepository.findAllByIsApply(false)).thenReturn(applications);

        List<PersonApplyOnCourseDto> result = personApplicationService.getNotApplyPerson();

        assertEquals(2, result.size());

        PersonApplyOnCourseDto dto1 = result.get(0);
        assertEquals(1L, dto1.getId());
        assertEquals("John Doe", dto1.getName());
        assertEquals("course-slug-1", dto1.getCourseSlug());

        PersonApplyOnCourseDto dto2 = result.get(1);
        assertEquals(2L, dto2.getId());
        assertEquals("Jane Smith", dto2.getName());
        assertEquals("course-slug-2", dto2.getCourseSlug());
    }

    @Test
    void testGetApplicationByPersonAndCourse() {
        PersonEntity person = new PersonEntity();
        CourseEntity course = new CourseEntity();
        String courseSlug = "sample-course-slug";

        when(courseRepository.findBySlug(courseSlug))
                .thenReturn(Optional.of(course));

        PersonApplicationEntity application = new PersonApplicationEntity();
        application.setPerson(person);
        application.setCourse(course);

        when(personApplicationRepository.findByPersonAndCourse(person, course))
                .thenReturn(application);

        PersonApplicationEntity result =
                personApplicationService
                        .getApplicationByPersonAndCourse(person, courseSlug);

        assertNotNull(result);
        assertEquals(person, result.getPerson());
        assertEquals(course, result.getCourse());
    }

    @Test
    void testGetApplicationByPersonAndCourseWithNonExistingCourse() {
        PersonEntity person = new PersonEntity();
        String courseSlug = "non-existing-course";

        when(courseRepository.findBySlug(courseSlug))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> personApplicationService
                        .getApplicationByPersonAndCourse(person, courseSlug));
    }

    @Test
    void testAmIpresentWhenPersonIsNull() {
        String courseSlug = "example-course-slug";
        PersonEntity person = null;

        Optional<Boolean> result = personApplicationService
                .amIpresent(courseSlug, person);

        assertTrue(result.isEmpty());
    }

    @Test
    void testAmIpresentWhenCourseNotFound() {
        String courseSlug = "non-existing-course";
        PersonEntity person = new PersonEntity();

        when(courseRepository.findBySlug(courseSlug))
                .thenReturn(Optional.empty());

        Optional<Boolean> result = personApplicationService
                .amIpresent(courseSlug, person);

        assertTrue(result.isEmpty());
    }

    @Test
    void testAmIpresentWhenPersonNotApplied() {
        String courseSlug = "example-course-slug";
        PersonEntity person = new PersonEntity();

        when(courseRepository.findBySlug(courseSlug))
                .thenReturn(Optional.of(new CourseEntity()));
        when(personApplicationRepository
                .findByPersonAndCourse(person, new CourseEntity()))
                .thenReturn(null);

        Optional<Boolean> result = personApplicationService
                .amIpresent(courseSlug, person);

        assertTrue(result.isPresent());
        assertFalse(result.get());
    }

    @Test
    void testAmIpresentWhenPersonApplied() {
        String courseSlug = "example-course-slug";
        PersonEntity person = new PersonEntity();

        when(courseRepository.findBySlug(courseSlug)).thenReturn(Optional.of(new CourseEntity()));
        when(personApplicationRepository
                .findByPersonAndCourse(person, new CourseEntity()))
                .thenReturn(new PersonApplicationEntity());

        Optional<Boolean> result = personApplicationService
                .amIpresent(courseSlug, person);

        assertTrue(result.isPresent());
    }
}