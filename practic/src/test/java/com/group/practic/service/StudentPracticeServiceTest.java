package com.group.practic.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.group.practic.entity.ChapterEntity;
import com.group.practic.entity.ChapterPartEntity;
import com.group.practic.entity.PersonEntity;
import com.group.practic.entity.StudentPracticeEntity;
import com.group.practic.enumeration.PracticeState;
import com.group.practic.repository.StudentPracticeRepository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@Slf4j
@ExtendWith(MockitoExtension.class)
class StudentPracticeServiceTest {

    @Mock
    private StudentPracticeRepository studentPracticeRepository;

    @Mock
    private ChapterPartService chapterPartService;
    @InjectMocks

    private StudentPracticeService studentPracticeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        studentPracticeService = new StudentPracticeService(
                studentPracticeRepository, chapterPartService);
    }

    @Test
    void testGetAllStudentsByState() {
        StudentPracticeEntity student1 = new StudentPracticeEntity();
        student1.setId(1L);
        student1.setState(PracticeState.NOT_STARTED);
        StudentPracticeEntity student2 = new StudentPracticeEntity();
        student2.setId(2L);
        student2.setState(PracticeState.NOT_STARTED);

        List<StudentPracticeEntity> expectedStudents = new ArrayList<>();
        expectedStudents.add(student1);
        expectedStudents.add(student2);
        when(studentPracticeRepository.findByState(PracticeState.NOT_STARTED))
                .thenReturn(expectedStudents);
        List<StudentPracticeEntity> students =
                studentPracticeService.getAllStudentsByState(PracticeState.NOT_STARTED);
        assertEquals(expectedStudents, students);
        verify(studentPracticeRepository, times(1)).findByState(PracticeState.NOT_STARTED);
    }

    @Test
    void testGetAllPracticesByChapter() {

        PersonEntity student = new PersonEntity();
        ChapterEntity chapter = new ChapterEntity();

        StudentPracticeEntity practice1 = new StudentPracticeEntity();
        practice1.setId(1L);
        practice1.setStudent(student);
        practice1.setChapter(chapter);

        StudentPracticeEntity practice2 = new StudentPracticeEntity();
        practice2.setId(2L);
        practice2.setStudent(student);
        practice2.setChapter(chapter);

        Set<StudentPracticeEntity> expectedPractices = new HashSet<>();
        expectedPractices.add(practice1);
        expectedPractices.add(practice2);

        when(studentPracticeRepository.findByStudentAndChapter(student, chapter))
                .thenReturn(expectedPractices);
        Set<StudentPracticeEntity> practices = studentPracticeService
                .getAllPracticesByChapter(student, chapter);
        assertEquals(expectedPractices, practices);
        verify(studentPracticeRepository, times(1)).findByStudentAndChapter(student, chapter);
    }


    @Test
    void testAddPractice() {

        PersonEntity student = new PersonEntity();
        ChapterPartEntity chapterPart = new ChapterPartEntity();
        ChapterEntity chapter = new ChapterEntity();

        StudentPracticeEntity expectedPractice = new StudentPracticeEntity();
        expectedPractice.setStudent(student);
        expectedPractice.setChapterPart(chapterPart);
        expectedPractice.setChapter(chapter);
        expectedPractice.setState(PracticeState.NOT_STARTED);

        when(studentPracticeRepository.save(any(StudentPracticeEntity.class)))
                .thenReturn(expectedPractice);

        StudentPracticeEntity practice = studentPracticeService
                .addPractice(student, chapterPart, chapter);

        verify(studentPracticeRepository, times(1))
                .save(argThat(arg -> arg.getStudent() == student
                        && arg.getChapterPart() == chapterPart
                        && arg.getChapter() == chapter
                        && arg.getState() == PracticeState.NOT_STARTED));

        assertEquals(expectedPractice, practice);
    }

    @Test
    void testGetPractice() {
        PersonEntity student = new PersonEntity();
        long chapterPartId = 123;
        ChapterPartEntity chapterPart = new ChapterPartEntity();
        chapterPart.setId(chapterPartId);
        StudentPracticeEntity expectedPractice = new StudentPracticeEntity();
        expectedPractice.setStudent(student);

        when(chapterPartService.getChapterPartById(chapterPartId)).thenReturn(chapterPart);
        when(studentPracticeRepository
                .findByStudentAndChapterPart(student, chapterPart))
                .thenReturn(expectedPractice);
        StudentPracticeEntity practice = studentPracticeService.getPractice(student, chapterPartId);

        verify(chapterPartService, times(1)).getChapterPartById(chapterPartId);
        verify(studentPracticeRepository, times(1))
                .findByStudentAndChapterPart(student, chapterPart);

        assertEquals(expectedPractice, practice);

    }

    @Test
    void testSave() {
        StudentPracticeEntity practiceToSave = new StudentPracticeEntity();
        StudentPracticeEntity savedPractice = new StudentPracticeEntity();
        when(studentPracticeRepository.save(practiceToSave)).thenReturn(savedPractice);
        StudentPracticeEntity result = studentPracticeService.save(practiceToSave);
        verify(studentPracticeRepository, times(1)).save(practiceToSave);
        assertEquals(savedPractice, result);
    }

    @Test
    void testGetAllPracticesByStudent() {
        PersonEntity student = new PersonEntity();

        Set<StudentPracticeEntity> expectedPractices = new HashSet<>();

        when(studentPracticeRepository.findAllByStudent(student)).thenReturn(expectedPractices);

        Set<StudentPracticeEntity> practices =
                studentPracticeService.getAllPracticesByStudent(student);

        verify(studentPracticeRepository, times(1)).findAllByStudent(student);

        assertEquals(expectedPractices, practices);
    }
}