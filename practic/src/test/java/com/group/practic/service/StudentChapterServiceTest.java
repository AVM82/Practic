package com.group.practic.service;

import com.group.practic.entity.*;
import com.group.practic.exception.ResourceNotFoundException;
import com.group.practic.repository.StudentChapterRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@Slf4j

public class StudentChapterServiceTest {

    @Mock
    private StudentChapterRepository studentChapterRepository;
    @Mock
    private ChapterService chapterService;
    @Mock
    private StudentOnCourseService studentOnCourseService;
    @Mock
    private ChapterPartService chapterPartService;

    @InjectMocks

    private StudentChapterService studentChapterService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testFindOpenChapters() {
        // Arrange
        PersonEntity student = new PersonEntity();
        StudentChapterEntity chapter1 = new StudentChapterEntity();
        StudentChapterEntity chapter2 = new StudentChapterEntity();
        Set<StudentChapterEntity> expectedChapters = new HashSet<>();
        expectedChapters.add(chapter1);
        expectedChapters.add(chapter2);

        when(studentChapterRepository.findByStudent(student)).thenReturn(expectedChapters);

        Set<StudentChapterEntity> openChapters = studentChapterService.findOpenChapters(student);

        assertEquals(expectedChapters, openChapters);
    }


    @Test
    public void testAddChapterWhenChapterDoesNotExist() {
        long studentId = 1L;
        long chapterId = 2L;

        when(chapterService.get(chapterId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> studentChapterService.addChapter(studentId, chapterId));
    }

    @Test
    public void testAddChapterWhenStudentDoesNotExist() {
        long studentId = 1L;
        long chapterId = 2L;

        when(chapterService.get(chapterId)).thenReturn(Optional.of(new ChapterEntity()));
        when(studentOnCourseService.get(studentId)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> studentChapterService.addChapter(studentId, chapterId));
    }



}