package com.group.practic.service;

import com.group.practic.dto.PersonApplyOnCourseDto;
import com.group.practic.entity.CourseEntity;
import com.group.practic.entity.PersonApplicationEntity;
import com.group.practic.entity.PersonEntity;
import com.group.practic.exception.ResourceNotFoundException;
import com.group.practic.repository.CourseRepository;
import com.group.practic.repository.PersonApplicationRepository;
import com.group.practic.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

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

        assertThrows(ResourceNotFoundException.class, () -> personApplicationService.addPersonApplication(person, courseSlug));
    }

    @Test
    void testGetNotApplyPerson() {
        PersonApplicationEntity application1 = new PersonApplicationEntity();
        application1.setApply(false);
        PersonApplicationEntity application2 = new PersonApplicationEntity();
        application2.setApply(false);

        when(personApplicationRepository.findAllByIsApply(false)).thenReturn(List.of(application1, application2));

        List<PersonApplyOnCourseDto> result = personApplicationService.getNotApplyPerson();

        assertEquals(2, result.size());
    }

    @Test
    void testGetApplicationByPersonAndCourse() {
        PersonEntity person = new PersonEntity();
        CourseEntity course = new CourseEntity();
        String courseSlug = "sample-course-slug";

        when(courseRepository.findBySlug(courseSlug)).thenReturn(Optional.of(course));

        PersonApplicationEntity application = new PersonApplicationEntity();
        application.setPerson(person);
        application.setCourse(course);

        when(personApplicationRepository.findByPersonAndCourse(person, course)).thenReturn(application);

        PersonApplicationEntity result = personApplicationService.getApplicationByPersonAndCourse(person, courseSlug);

        assertNotNull(result);
        assertEquals(person, result.getPerson());
        assertEquals(course, result.getCourse());
    }

    @Test
    void testGetApplicationByPersonAndCourseWithNonExistingCourse() {
        PersonEntity person = new PersonEntity();
        String courseSlug = "non-existing-course";

        when(courseRepository.findBySlug(courseSlug)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> personApplicationService.getApplicationByPersonAndCourse(person, courseSlug));
    }
}