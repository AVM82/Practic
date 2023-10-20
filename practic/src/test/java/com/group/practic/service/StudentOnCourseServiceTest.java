package com.group.practic.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.group.practic.dto.ChapterDto;
import com.group.practic.entity.AdditionalMaterialsEntity;
import com.group.practic.entity.ChapterEntity;
import com.group.practic.entity.CourseEntity;
import com.group.practic.entity.PersonEntity;
import com.group.practic.entity.StudentOnCourseEntity;
import com.group.practic.repository.PersonApplicationRepository;
import com.group.practic.repository.RoleRepository;
import com.group.practic.repository.StudentOnCourseRepository;
import com.group.practic.util.Converter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;



@Slf4j
class StudentOnCourseServiceTest {

    @InjectMocks
    private StudentOnCourseService studentOnCourseService;

    @Mock
    private StudentOnCourseRepository studentOnCourseRepository;
    @Mock
    private ChapterEntity chapterEntity;
    @Mock
    private CourseService courseService;

    @Mock
    private PersonService personService;

    @Mock
    private RoleRepository roleRepository;
    @Mock

    private ChapterService chapterService;
    @Mock
    private PersonApplicationRepository personApplicationRepository;
    @Mock
    EmailSenderService emailSenderService;
    @Mock
    private CourseEntity courseEntity;
    @Mock
    private StudentOnCourseEntity studentOnCourseEntity;
    @Mock
    private Converter converter;

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
    void testGetStudentOnCourse() {
        long courseId = 1;
        long studentId = 2;
        boolean inactive = false;
        boolean ban = false;

        CourseEntity course = new CourseEntity();
        when(courseService.get(courseId)).thenReturn(Optional.of(course));

        PersonEntity student = new PersonEntity();
        when(personService.get(studentId)).thenReturn(Optional.of(student));

        StudentOnCourseEntity expectedStudentOnCourse = new StudentOnCourseEntity(student, course);
        when(studentOnCourseRepository
                .findByCourseAndStudentAndInactiveAndBan(course, student, inactive, ban))
                .thenReturn(expectedStudentOnCourse);

        StudentOnCourseEntity result
                = studentOnCourseService.get(courseId, studentId, inactive, ban);

        assertEquals(expectedStudentOnCourse, result);
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
    void testCreateStudentOnCourseStudentIsNotPresent() {
        long courseId = 1L;
        long studentId = 2L;
        when(courseService.get(courseId)).thenReturn(Optional.empty());
        when(personService.get(studentId)).thenReturn(Optional.empty());

        Optional<StudentOnCourseEntity> result = studentOnCourseService.create(courseId, studentId);

        assertNotNull(result);
        assertFalse(result.isPresent());

        reset(personService);

        verifyNoInteractions(personService, studentOnCourseRepository,
                personApplicationRepository, roleRepository, emailSenderService);
    }

    @Test
    void testNotify() {
        PersonEntity student = new PersonEntity();
        String slug = "example-slug";

        StudentOnCourseRepository studentOnCourseRepository
                = mock(StudentOnCourseRepository.class);
        CourseService courseService = mock(CourseService.class);
        ChapterService chapterService = mock(ChapterService.class);
        AdditionalMaterialsService additionalMaterialsService
                = mock(AdditionalMaterialsService.class);
        PersonService personService = mock(PersonService.class);
        RoleRepository roleRepository = mock(RoleRepository.class);
        PersonApplicationRepository personApplicationRepository
                = mock(PersonApplicationRepository.class);
        EmailSenderService emailSenderService
                = mock(EmailSenderService.class);

        StudentOnCourseService studentOnCourseService
                = new StudentOnCourseService(
                studentOnCourseRepository, courseService, personService,
                roleRepository, personApplicationRepository, chapterService,
                emailSenderService, additionalMaterialsService
        );

        ReflectionTestUtils.invokeMethod(studentOnCourseService, "notify", student, slug);

        verify(emailSenderService).sendMessage(any());
    }

    @Test
    void testGetStudentOfCourse() {
        CourseEntity course = new CourseEntity();
        PersonEntity person = new PersonEntity();

        when(personService.getPerson()).thenReturn(person);
        when(studentOnCourseRepository.findByCourseAndStudentAndInactiveAndBan(
                course, person, false, false)).thenReturn(new StudentOnCourseEntity());

        StudentOnCourseEntity result = studentOnCourseService.getStudentOfCourse(course);

        assertNotNull(result);
    }

    @Test
    void testIsStudentofCourse() {
        CourseEntity course = new CourseEntity();
        PersonEntity person = new PersonEntity();

        when(personService.getPerson()).thenReturn(person);
        when(studentOnCourseRepository.findByCourseAndStudentAndInactiveAndBan(
                course, person, false, false)).thenReturn(new StudentOnCourseEntity());

        boolean result = studentOnCourseService.isStudentofCourse(course);
        assertTrue(result);
    }


    @Test
    void testGetActiveChapterNumber() {
        StudentOnCourseEntity studentOnCourse = new StudentOnCourseEntity();
        studentOnCourse.setActiveChapter(chapterEntity);

        Mockito.when(personService.hasAdvancedRole()).thenReturn(false);
        Mockito.when(personService.amImentor(courseEntity)).thenReturn(false);

        int result = studentOnCourseService.getActiveChapterNumber(courseEntity);

        assertEquals(chapterEntity.getNumber(), result);
    }

    @Test
    void testGetActiveChapterNumberAsAdvancedUser() {
        CourseEntity course = new CourseEntity();
        when(personService.hasAdvancedRole()).thenReturn(true);

        Integer result = studentOnCourseService.getActiveChapterNumber(course);

        assertEquals(Integer.MAX_VALUE, result);
    }

    @Test
    void testGetActiveChapterNumberAsMentor() {
        CourseEntity course = new CourseEntity();
        when(personService.hasAdvancedRole()).thenReturn(false);
        when(personService.amImentor(course)).thenReturn(true);

        Integer result = studentOnCourseService.getActiveChapterNumber(course);

        assertEquals(Integer.MAX_VALUE, result);
    }

    @Test
    void testGetOpenedChapter() {
        ChapterService chapterService = mock(ChapterService.class);
        CourseService courseService = mock(CourseService.class);
        PersonService personService = mock(PersonService.class);
        AdditionalMaterialsService additionalMaterialsService
                = mock(AdditionalMaterialsService.class);


        StudentOnCourseService studentOnCourseService = new StudentOnCourseService(
                studentOnCourseRepository, courseService, personService, roleRepository,
                personApplicationRepository, chapterService, emailSenderService,
                additionalMaterialsService
        );

        CourseEntity course = new CourseEntity("course_slug", "Course Name", "svg");
        ChapterEntity chapter = new ChapterEntity(1, course, 1, "Chapter 1", "Chapter One");
        when(chapterService.getChapterByNumber(course, 1)).thenReturn(Optional.of(chapter));

        when(personService.amImentor(course)).thenReturn(true);

        Optional<ChapterEntity> openedChapter = studentOnCourseService.getOpenedChapter(course, 1);

        assertTrue(openedChapter.isPresent());
        assertEquals(chapter, openedChapter.get());

        reset(chapterService);
        reset(courseService);
        reset(personService);
    }

    @Test
    void testChangeStudentAdditionalMaterial() {

        AdditionalMaterialsService additionalMaterialsService
                = mock(AdditionalMaterialsService.class);
        StudentOnCourseRepository studentOnCourseRepository
                = mock(StudentOnCourseRepository.class);


        StudentOnCourseService studentOnCourseService = new StudentOnCourseService(
                studentOnCourseRepository, courseService, personService, roleRepository,
                personApplicationRepository, chapterService, emailSenderService,
                additionalMaterialsService
        );
        AdditionalMaterialsEntity additionalMaterial = new AdditionalMaterialsEntity();
        StudentOnCourseEntity student = new StudentOnCourseEntity();

        when(additionalMaterialsService.get(1L)).thenReturn(Optional.of(additionalMaterial));

        Optional<Boolean> result
                = studentOnCourseService
                .changeStudentAdditionalMaterial(student, 1L, true);

        assertTrue(result.isPresent());
        assertTrue(result.get());


        verify(studentOnCourseRepository).save(student);
    }

    @Test
    void testGetLastVisibleChapterNumber1() {


        CourseEntity course = new CourseEntity();
        ChapterEntity chapter = new ChapterEntity();
        chapter.setNumber(3);
        PersonEntity person = new PersonEntity();

        doReturn(false).when(personService).hasAdvancedRole();
        doReturn(false).when(personService).amImentor(course);
        doReturn(person).when(personService).getPerson();
        StudentOnCourseEntity studentOnCourse = Mockito.mock(StudentOnCourseEntity.class);

        when(studentOnCourseService.getStudentOfCourse(course)).thenReturn(studentOnCourse);

        when(studentOnCourse.getActiveChapter()).thenReturn(chapter);

        int lastVisibleChapterNumber = studentOnCourseService.getLastVisibleChapterNumber(course);

        assertEquals(3, lastVisibleChapterNumber);
    }

    @Test
    void testGetChaptersWhenCourseIsNotEmpty() {

        String slug = "example-slug";
        CourseEntity course = new CourseEntity();
        when(courseService.get(slug)).thenReturn(Optional.of(course));

        List<ChapterEntity> chapters = new ArrayList<>();
        ChapterEntity chapter1 = new ChapterEntity();
        ChapterEntity chapter2 = new ChapterEntity();
        chapters.add(chapter1);
        chapters.add(chapter2);
        when(chapterService.getAll(course)).thenReturn(chapters);


        List<ChapterDto> expectedChapters = new ArrayList<>();
        expectedChapters.add(new ChapterDto(chapter1, true));
        expectedChapters.add(new ChapterDto(chapter2, true));


        List<ChapterDto> result = studentOnCourseService.getChapters(slug);


        assertEquals(expectedChapters.size(), result.size());
        for (int i = 0; i < expectedChapters.size(); i++) {
            assertEquals(expectedChapters.get(i).getId(), result.get(i).getId());
            assertEquals(expectedChapters.get(i).getNumber(), result.get(i).getNumber());
            assertEquals(expectedChapters.get(i).getShortName(), result.get(i).getShortName());
            assertEquals(expectedChapters.get(i).isVisible(), result.get(i).isVisible());

        }
    }

    @Test
    void testGetChaptersWhenCourseIsEmpty() {
        String slug = "example-slug";
        when(courseService.get(slug)).thenReturn(Optional.empty());
        List<ChapterDto> result = studentOnCourseService.getChapters(slug);
        assertEquals(List.of(), result);
    }
}