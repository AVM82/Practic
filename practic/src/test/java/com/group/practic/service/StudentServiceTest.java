package com.group.practic.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.group.practic.entity.ChapterEntity;
import com.group.practic.entity.CourseEntity;
import com.group.practic.entity.PersonEntity;
import com.group.practic.entity.StudentChapterEntity;
import com.group.practic.entity.StudentEntity;
import com.group.practic.enumeration.ChapterState;
import com.group.practic.repository.StudentChapterRepository;
import com.group.practic.repository.StudentRepository;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


class StudentServiceTest {

    String slug = "example-slug";

    @InjectMocks
    private StudentService studentService;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private StudentChapterRepository studentChapterRepository;

    @Mock
    private CourseService courseService;

    @Mock
    private PersonService personService;

    @Mock
    EmailSenderService emailSenderService;

    @Mock
    private PersonEntity personEntity;

    @Mock
    private ChapterEntity chapterEntity;

    @Mock
    private CourseEntity courseEntity;

    @Mock
    private StudentEntity studentEntity;

    

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testAllowToCloseChapter() {
        StudentChapterEntity studentChapter = new StudentChapterEntity();
        assertFalse(studentService.allowToCloseChapter(studentChapter));
        studentChapter.setState(ChapterState.PAUSE);
        assertFalse(studentService.allowToCloseChapter(studentChapter));
        studentChapter.setState(ChapterState.DONE);
        assertFalse(studentService.allowToCloseChapter(studentChapter));
        studentChapter.setState(ChapterState.IN_PROCESS);
        // add conditions
        assertTrue(studentService.allowToCloseChapter(studentChapter));
    }


    @Test
    void testChangeChapterStateNotStarted() {
        StudentChapterEntity studentChapter = new StudentChapterEntity();
        when(studentChapterRepository.save(studentChapter)).thenReturn(studentChapter);

        assertEquals(ChapterState.NOT_STARTED, studentService
                .changeChapterState(studentChapter, ChapterState.NOT_STARTED).getState());
        assertEquals(ChapterState.NOT_STARTED,
                studentService.changeChapterState(studentChapter, ChapterState.DONE).getState());
        assertEquals(ChapterState.NOT_STARTED,
                studentService.changeChapterState(studentChapter, ChapterState.PAUSE).getState());
        assertEquals(ChapterState.IN_PROCESS, studentService
                .changeChapterState(studentChapter, ChapterState.IN_PROCESS).getState());
    }


    @Test
    void testChangeChapterStatePause() {
        StudentChapterEntity studentChapter = new StudentChapterEntity();
        studentChapter.setState(ChapterState.PAUSE);
        when(studentChapterRepository.save(studentChapter)).thenReturn(studentChapter);
        
        assertEquals(ChapterState.PAUSE, studentService
                .changeChapterState(studentChapter, ChapterState.NOT_STARTED).getState());
        assertEquals(ChapterState.PAUSE,
                studentService.changeChapterState(studentChapter, ChapterState.PAUSE).getState());
        assertEquals(ChapterState.PAUSE,
                studentService.changeChapterState(studentChapter, ChapterState.DONE).getState());
        assertEquals(ChapterState.IN_PROCESS, studentService
                .changeChapterState(studentChapter, ChapterState.IN_PROCESS).getState());
    }


    @Test
    void testChangeChapterStateInProcess() {
        StudentChapterEntity studentChapter = new StudentChapterEntity();
        studentChapter.setState(ChapterState.IN_PROCESS);
        studentChapter.setStartCounting(LocalDate.now());
        when(studentChapterRepository.save(studentChapter)).thenReturn(studentChapter);

        assertEquals(ChapterState.IN_PROCESS, studentService
                .changeChapterState(studentChapter, ChapterState.NOT_STARTED).getState());
        assertEquals(ChapterState.IN_PROCESS, studentService
                .changeChapterState(studentChapter, ChapterState.IN_PROCESS).getState());
        assertEquals(ChapterState.PAUSE,
                studentService.changeChapterState(studentChapter, ChapterState.PAUSE).getState());
        // set conditions to false
        // assertEquals(ChapterState.IN_PROCESS, studentService
        // .changeChapterState(studentChapter, ChapterState.DONE).getState());

        // add conditions
        when(studentChapterRepository.save(studentChapter)).thenReturn(studentChapter);
        assertEquals(ChapterState.PAUSE,
                studentService.changeChapterState(studentChapter, ChapterState.DONE).getState());
    }


    @Test
    void testChangeChapterStateDone() {
        StudentChapterEntity studentChapter = new StudentChapterEntity();
        studentChapter.setState(ChapterState.DONE);

        assertEquals(ChapterState.DONE, studentService
                .changeChapterState(studentChapter, ChapterState.NOT_STARTED).getState());
        assertEquals(ChapterState.DONE, studentService
                .changeChapterState(studentChapter, ChapterState.IN_PROCESS).getState());
        assertEquals(ChapterState.DONE,
                studentService.changeChapterState(studentChapter, ChapterState.PAUSE).getState());
    }

    
    @Test
    void testStart() {
        StudentChapterEntity studentChapter = 
                new StudentChapterEntity(studentEntity, chapterEntity);
        studentChapter.setNumber(1);
        assertEquals(ChapterState.IN_PROCESS, studentService
                .changeChapterState(studentChapter, ChapterState.IN_PROCESS).getState());
        verify(studentEntity).setStart(any());
    }


    @Test
    void testFinish() {

    }

}
