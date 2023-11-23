//package com.group.practic.service;
//
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.when;
//
//import com.group.practic.entity.ChapterEntity;
//import com.group.practic.entity.ChapterPartEntity;
//import com.group.practic.entity.CourseEntity;
//import com.group.practic.entity.PersonEntity;
//import com.group.practic.entity.StudentChapterEntity;
//import com.group.practic.entity.StudentOnCourseEntity;
//import com.group.practic.entity.StudentPracticeEntity;
//import com.group.practic.exception.ResourceNotFoundException;
//import com.group.practic.repository.StudentChapterRepository;
//import java.util.HashSet;
//import java.util.Optional;
//import java.util.Set;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//@Slf4j
//class StudentChapterServiceTest {
//
//    @Mock
//    private StudentChapterRepository studentChapterRepository;
//    @Mock
//    private ChapterService chapterService;
//    @Mock
//    private StudentOnCourseService studentOnCourseService;
//    @Mock
//    private ChapterPartService chapterPartService;
//
//    @InjectMocks
//
//    private StudentChapterService studentChapterService;
//
//    @BeforeEach
//    public void setup() {
//        MockitoAnnotations.initMocks(this);
//    }
//
//
//    @Mock
//    private StudentPracticeService studentPracticeService;
//
//
//    @Test
//    void testFindOpenChapters() {
//        PersonEntity student = new PersonEntity();
//        StudentChapterEntity chapter1 = new StudentChapterEntity();
//        StudentChapterEntity chapter2 = new StudentChapterEntity();
//        Set<StudentChapterEntity> expectedChapters = new HashSet<>();
//        expectedChapters.add(chapter1);
//        expectedChapters.add(chapter2);
//
//        when(studentChapterRepository.findByStudent(student)).thenReturn(expectedChapters);
//
//        Set<StudentChapterEntity> openChapters = studentChapterService.findOpenChapters(student);
//
//        Assertions.assertEquals(expectedChapters, openChapters);
//    }
//
//    @Test
//    void testAddChapterWhenChapterDoesNotExist() {
//        long studentId = 1L;
//        long chapterId = 2L;
//
//        when(chapterService.get(chapterId)).thenReturn(Optional.empty());
//
//        assertThrows(ResourceNotFoundException.class,
//                () -> studentChapterService.addChapter(studentId, chapterId));
//    }
//
//    @Test
//    void testAddChapterWhenStudentDoesNotExist() {
//        long studentId = 1L;
//        long chapterId = 2L;
//
//        when(chapterService.get(chapterId)).thenReturn(Optional.of(new ChapterEntity()));
//        when(studentOnCourseService.get(studentId)).thenReturn(null);
//
//        assertThrows(ResourceNotFoundException.class,
//                () -> studentChapterService.addChapter(studentId, chapterId));
//    }
//
//    @Test
//    void testAddPractices() {
//        ChapterPartEntity chapterPart1 = new ChapterPartEntity();
//        Set<ChapterPartEntity> chapterParts = new HashSet<>();
//        chapterParts.add(chapterPart1);
//
//        long chapterId = 2L;
//        when(chapterPartService.getAllPractices(chapterId)).thenReturn(Optional.of(chapterParts));
//        ChapterEntity chapter = new ChapterEntity();
//        when(chapterService.get(chapterId)).thenReturn(Optional.of(chapter));
//        PersonEntity student = new PersonEntity();
//        StudentOnCourseEntity studentOnCourse =
//                new StudentOnCourseEntity(student, new CourseEntity());
//        long studentId = 1L;
//        when(studentOnCourseService.get(studentId)).thenReturn(studentOnCourse);
//        when(studentPracticeService.addPractice(student, chapterPart1, chapter))
//                .thenReturn(new StudentPracticeEntity());
//        Set<StudentPracticeEntity> practices =
//                studentChapterService.addPractices(studentId, chapterId);
//        assertNotNull(practices);
//        Assertions.assertEquals(1, practices.size());
//    }
//
//}