package com.group.practic.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.group.practic.entity.CourseEntity;
import com.group.practic.entity.PersonApplicationEntity;
import com.group.practic.entity.PersonEntity;
import com.group.practic.entity.RoleEntity;
import com.group.practic.entity.StudentOnCourseEntity;
import com.group.practic.repository.PersonApplicationRepository;
import com.group.practic.repository.RoleRepository;
import com.group.practic.repository.StudentOnCourseRepository;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@Slf4j
class StudentOnCourseServiceTest {
/*
    @InjectMocks
    private StudentOnCourseService studentOnCourseService;

    @Mock
    private StudentOnCourseRepository studentOnCourseRepository;

    @Mock
    private CourseService courseService;

    @Mock
    private PersonService personService;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PersonApplicationRepository personApplicationRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllStudentCourseRelationships() {
        List<StudentOnCourseEntity> expectedStudentCourseRelationships = List.of(
                new StudentOnCourseEntity(),
                new StudentOnCourseEntity()
        );

        when(studentOnCourseRepository.findAll())
                .thenReturn(expectedStudentCourseRelationships);
        List<StudentOnCourseEntity> result = studentOnCourseService.get();
        assertEquals(expectedStudentCourseRelationships, result);
    }

    @Test
    void testGetStudentCourseRelationshipById() {
        long studentId = 1L;
        StudentOnCourseEntity expectedStudentCourseRelationship = new StudentOnCourseEntity();
        when(studentOnCourseRepository.findByStudentId(studentId))
                .thenReturn(expectedStudentCourseRelationship);
        StudentOnCourseEntity result = studentOnCourseService.get(studentId);
        assertEquals(expectedStudentCourseRelationship, result);
    }

    @Test
    void testGetAllStudentOnCourses() {
        List<StudentOnCourseEntity> expectedStudentOnCourses = List.of(
                new StudentOnCourseEntity(), new StudentOnCourseEntity());
        when(studentOnCourseRepository.findAll()).thenReturn(expectedStudentOnCourses);
        List<StudentOnCourseEntity> result = studentOnCourseService.get();
        assertEquals(expectedStudentOnCourses, result);
    }

    @Test
    void testGetStudentCourseRelationshipsByInactiveAndBan() {
        boolean inactive = true;
        boolean ban = false;
        List<StudentOnCourseEntity> expectedStudentCourseRelationships = List.of(
                new StudentOnCourseEntity(),
                new StudentOnCourseEntity()
        );

        when(studentOnCourseRepository.findAllByInactiveAndBan(inactive, ban))
                .thenReturn(expectedStudentCourseRelationships);

        List<StudentOnCourseEntity> result = studentOnCourseService.get(inactive, ban);

        assertEquals(expectedStudentCourseRelationships, result);
    }

    @Test
    void testGetStudentCourseRelationshipsByCourseAndStudent() {
        long courseId = 1L;
        long studentId = 2L;
        boolean inactive = true;
        boolean ban = false;

        CourseEntity courseEntity = new CourseEntity();
        PersonEntity studentEntity = new PersonEntity();

        List<StudentOnCourseEntity> expectedStudentCourseRelationships = List.of(
                new StudentOnCourseEntity(),
                new StudentOnCourseEntity()
        );

        when(courseService.get(courseId)).thenReturn(Optional.of(courseEntity));
        when(personService.get(studentId)).thenReturn(Optional.of(studentEntity));
        when(studentOnCourseRepository
                .findAllByCourseAndStudentAndInactiveAndBan(courseEntity, studentEntity,
                        inactive, ban))
                .thenReturn(expectedStudentCourseRelationships);

        List<StudentOnCourseEntity> result =
                studentOnCourseService.get(courseId, studentId, inactive, ban);

        assertEquals(expectedStudentCourseRelationships, result);
    }

    @Test
    void testGetCoursesOfStudent() {
        long studentId = 1L;
        boolean inactive = true;
        boolean ban = false;

        PersonEntity studentEntity = new PersonEntity();

        List<StudentOnCourseEntity> expectedCoursesOfStudent = List.of(
                new StudentOnCourseEntity(),
                new StudentOnCourseEntity()
        );
        when(personService.get(studentId)).thenReturn(Optional.of(studentEntity));

        when(studentOnCourseRepository
                .findAllByStudentAndInactiveAndBan(studentEntity, inactive, ban))
                .thenReturn(expectedCoursesOfStudent);

        List<StudentOnCourseEntity> result =
                studentOnCourseService.getCoursesOfStudent(studentId, inactive, ban);

        assertEquals(expectedCoursesOfStudent, result);
    }

    @Test
    void testGetStudentsOfCourse() {
        long courseId = 1L;
        boolean inactive = true;
        boolean ban = false;

        CourseEntity courseEntity = new CourseEntity();

        List<StudentOnCourseEntity> expectedStudentsOfCourse = List.of(
                new StudentOnCourseEntity(),
                new StudentOnCourseEntity()
        );

        when(courseService.get(courseId)).thenReturn(Optional.of(courseEntity));
        when(studentOnCourseRepository
                .findAllByCourseAndInactiveAndBan(courseEntity, inactive, ban))
                .thenReturn(expectedStudentsOfCourse);

        List<StudentOnCourseEntity> result = studentOnCourseService
                .getStudentsOfCourse(courseId, inactive, ban);

        assertEquals(expectedStudentsOfCourse, result);
    }

    @Test
    void testCreateStudentOnCourse() {
        PersonEntity user = new PersonEntity();
        user.setName("John Doe");
        user.setLinkedin("linkedin-url");
        CourseEntity courseEntity =
                new CourseEntity("course-slug", "Course Short Name", "Course Name", "SVG");
        PersonEntity studentEntity = new PersonEntity();

        long courseId = 1L;
        long studentId = 2L;
        when(courseService.get(courseId)).thenReturn(Optional.of(courseEntity));
        when(personService.get(studentId)).thenReturn(Optional.of(studentEntity));

        StudentOnCourseEntity studentOnCourseEntity = new StudentOnCourseEntity(user, courseEntity);
        when(studentOnCourseRepository.save(studentOnCourseEntity))
                .thenReturn(studentOnCourseEntity);

        PersonApplicationEntity applicant = new PersonApplicationEntity();
        when(personApplicationRepository.findByPersonAndCourse(studentEntity, courseEntity))
                .thenReturn(applicant);
        when(personApplicationRepository.save(applicant)).thenReturn(applicant);

        RoleEntity studentRole = new RoleEntity("STUDENT");
        RoleEntity courseRole = new RoleEntity(courseEntity.getSlug());
        when(roleRepository.findByName("STUDENT")).thenReturn(studentRole);
        when(roleRepository.findByName(courseEntity.getSlug())).thenReturn(courseRole);

        Optional<StudentOnCourseEntity> student =
                Optional.of(studentOnCourseRepository.save(studentOnCourseEntity));

        assertTrue(student.isPresent());
        assertEquals(studentOnCourseEntity, student.get());
    }
*/
}