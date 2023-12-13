package com.group.practic.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.group.practic.entity.ChapterEntity;
import com.group.practic.entity.ChapterPartEntity;
import com.group.practic.entity.CourseEntity;
import com.group.practic.entity.PersonEntity;
import com.group.practic.entity.StudentChapterEntity;
import com.group.practic.entity.StudentEntity;
import com.group.practic.entity.StudentPracticeEntity;
import com.group.practic.enumeration.ChapterState;
import com.group.practic.enumeration.PracticeState;
import com.group.practic.repository.StudentChapterRepository;
import com.group.practic.repository.StudentRepository;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


class StudentServiceTest {

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
        when(studentEntity.getPerson()).thenReturn(personEntity);
        when(studentEntity.getCourse()).thenReturn(courseEntity);
    }


    private <T> List<T> getSpecificSizeList(Class<T> type, int size) {
        List<T> result = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            try {
                result.add(type.getDeclaredConstructor().newInstance());
            } catch (NoSuchMethodException | InstantiationException
                     | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    private void setPractices(StudentChapterEntity studentChapter,
                              int totalCount, int approvedCount) {
        when(chapterEntity.getParts())
                .thenReturn(getSpecificSizeList(ChapterPartEntity.class, totalCount));
        List<StudentPracticeEntity> generatedPractices = new ArrayList<>();
        for (int i = 1; i <= totalCount; i++, approvedCount--) {
            StudentPracticeEntity practice = new StudentPracticeEntity(studentChapter, i);
            if (approvedCount > 0) {
                practice.setState(PracticeState.APPROVED);
            }
            generatedPractices.add(practice);
        }
        studentChapter.setPractices(generatedPractices);
    }


    @Test
    void testAllowToCloseChapter() {
        StudentChapterEntity studentChapter =
                new StudentChapterEntity(studentEntity, chapterEntity);
        assertFalse(studentService.allowToCloseChapter(studentChapter));
        studentChapter.setState(ChapterState.PAUSE);
        assertFalse(studentService.allowToCloseChapter(studentChapter));
        studentChapter.setState(ChapterState.DONE);
        assertFalse(studentService.allowToCloseChapter(studentChapter));
        studentChapter.setState(ChapterState.IN_PROCESS);
        setPractices(studentChapter, 4, 0);
        assertFalse(studentService.allowToCloseChapter(studentChapter));
        setPractices(studentChapter, 4, 1);
        assertFalse(studentService.allowToCloseChapter(studentChapter));
        setPractices(studentChapter, 4, 2);
        assertFalse(studentService.allowToCloseChapter(studentChapter));
        setPractices(studentChapter, 4, 3);
        assertFalse(studentService.allowToCloseChapter(studentChapter));
        setPractices(studentChapter, 4, 4);
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
        StudentChapterEntity studentChapter =
                new StudentChapterEntity(studentEntity, chapterEntity);
        studentChapter.setState(ChapterState.IN_PROCESS);
        studentChapter.setStartCounting(LocalDate.now());

        when(studentChapterRepository.save(studentChapter)).thenReturn(studentChapter);

        assertEquals(ChapterState.IN_PROCESS, studentService
                .changeChapterState(studentChapter, ChapterState.NOT_STARTED).getState());
        assertEquals(ChapterState.IN_PROCESS, studentService
                .changeChapterState(studentChapter, ChapterState.IN_PROCESS).getState());

        assertEquals(ChapterState.PAUSE,
                studentService.changeChapterState(studentChapter, ChapterState.PAUSE).getState());

        studentChapter.setState(ChapterState.IN_PROCESS);
        setPractices(studentChapter, 4, 0);
        assertEquals(ChapterState.IN_PROCESS,
                studentService.changeChapterState(studentChapter, ChapterState.DONE).getState());
        setPractices(studentChapter, 4, 1);
        assertEquals(ChapterState.IN_PROCESS,
                studentService.changeChapterState(studentChapter, ChapterState.DONE).getState());
        setPractices(studentChapter, 4, 2);
        assertEquals(ChapterState.IN_PROCESS,
                studentService.changeChapterState(studentChapter, ChapterState.DONE).getState());
        setPractices(studentChapter, 4, 3);
        assertEquals(ChapterState.IN_PROCESS,
                studentService.changeChapterState(studentChapter, ChapterState.DONE).getState());

        setPractices(studentChapter, 4, 4);
        when(studentEntity.getFinish()).thenReturn(LocalDate.of(2023, 12, 31));
        when(studentEntity.getStart()).thenReturn(LocalDate.of(2023, 1, 1));
        assertEquals(ChapterState.DONE,
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
        StudentChapterEntity studentChapter =
                new StudentChapterEntity(studentEntity, chapterEntity);
        studentChapter.setState(ChapterState.IN_PROCESS);
        studentChapter.setStartCounting(LocalDate.of(2023, 11, 1));
        setPractices(studentChapter, 4, 4);
        when(courseService.getNextChapterByNumber(any(), eq(0))).thenReturn(Optional.empty());
        when(studentEntity.getFinish()).thenReturn(LocalDate.of(2023, 12, 31));
        when(studentEntity.getStart()).thenReturn(LocalDate.of(2023, 1, 1));

        assertEquals(ChapterState.DONE,
                studentService.changeChapterState(studentChapter, ChapterState.DONE).getState());
        verify(studentEntity).setFinish(any());
        verify(studentEntity).setActiveChapterNumber(0);
        verify(studentEntity).setWeeks(52);
        verify(studentEntity).setDaysSpent(0);
        verify(emailSenderService).sendEmail(any(), any(), any());
    }


    @Test
    void testOpenNextChapter() {
        ChapterEntity chapter = new ChapterEntity();
        chapter.setNumber(1234567);
        chapter.setParts(List.of());
        when(studentChapterRepository.save(any(StudentChapterEntity.class)))
                .thenReturn(new StudentChapterEntity(studentEntity, chapter));
        when(studentEntity.getActiveChapterNumber()).thenReturn(1000);
        when(courseService.getNextChapterByNumber(any(), eq(1000)))
                .thenReturn(Optional.of(chapter));

        studentService.openNextChapter(studentEntity);
        ArgumentCaptor<StudentChapterEntity> argument =
                ArgumentCaptor.forClass(StudentChapterEntity.class);
        verify(studentChapterRepository).save(argument.capture());
        assertEquals(1234567, argument.getValue().getNumber());
        assertEquals(studentEntity, argument.getValue().getStudent());
        assertEquals(chapter, argument.getValue().getChapter());
        verify(emailSenderService).sendEmail(any(), any(), any());
    }

}
