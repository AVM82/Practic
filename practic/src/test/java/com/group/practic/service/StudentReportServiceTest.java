package com.group.practic.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.group.practic.dto.StudentReportCreationDto;
import com.group.practic.entity.ChapterEntity;
import com.group.practic.entity.CourseEntity;
import com.group.practic.entity.PersonEntity;
import com.group.practic.entity.QuizEntity;
import com.group.practic.entity.StudentReportEntity;
import com.group.practic.entity.TimeSlotEntity;
import com.group.practic.repository.StudentReportRepository;
import com.group.practic.repository.TimeSlotRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Slf4j
@ExtendWith(MockitoExtension.class)
class StudentReportServiceTest {
    @Mock
    private StudentReportRepository studentReportRepository;
    @Mock
    private CourseService courseService;
    @Mock
    private ChapterService chapterService;
    @Mock
    private TimeSlotRepository timeSlotRepository;
    @Mock
    private TimeSlotService timeSlotService;

    @InjectMocks
    private StudentReportService studentReportService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testGetAllStudentsActualReports() {
        String courseSlug = "example-slug";

        CourseEntity course = new CourseEntity();
        course.setChapters(new ArrayList<>(Arrays.asList(
                new ChapterEntity(1, course, 1, "Chapter 1", new QuizEntity()),
                new ChapterEntity(2, course, 2, "Chapter 2", new QuizEntity())
        )));

        List<StudentReportEntity> reportsForChapter1 = Arrays.asList(
                new StudentReportEntity(course.getChapters().iterator().next(),
                        new TimeSlotEntity(), "Report 1"),
                new StudentReportEntity(course.getChapters().iterator().next(),
                        new TimeSlotEntity(), "Report 2")
        );

        List<StudentReportEntity> reportsForChapter2 = Arrays.asList(
                new StudentReportEntity(course.getChapters().iterator().next(),
                        new PersonEntity(), new TimeSlotEntity(), "Report 3"),
                new StudentReportEntity(course.getChapters().iterator().next(),
                        new PersonEntity(), new TimeSlotEntity(), "Report 4")
        );

        when(courseService.get(courseSlug)).thenReturn(Optional.of((course)));
        when(studentReportRepository.findAllByStudentChapterChapterAndStateInOrderByTimeSlotId(
                course.getChapters().iterator().next(), StudentReportService.ACTUAL_STATES))
                .thenReturn(reportsForChapter1);
        when(studentReportRepository
                .findAllByChapterAndStateInOrderByTimeSlotId(course.getChapters()
                        .iterator().next(), StudentReportService.ACTUAL_STATES))
                .thenReturn(reportsForChapter2);

        List<List<StudentReportEntity>> result =
                studentReportService.getAllStudentsActualReports(courseSlug);
        assertEquals(reportsForChapter1, result.get(0));
    }

    @Test
    void testGetAllStudentsActualReports() {
        String courseSlug = "example-slug";

        CourseEntity course = new CourseEntity();
        course.setChapters(new HashSet<>(Arrays.asList(
                new ChapterEntity(1, course, 1, "Chapter 1", "Short Name 1"),
                new ChapterEntity(2, course, 2, "Chapter 2", "Short Name 2")
        )));

        List<StudentReportEntity> reportsForChapter1 = Arrays.asList(
                new StudentReportEntity(course.getChapters().iterator().next(),
                        new PersonEntity(), new TimeSlotEntity(), "Report 1"),
                new StudentReportEntity(course.getChapters().iterator().next(),
                        new PersonEntity(), new TimeSlotEntity(), "Report 2")
        );

        List<StudentReportEntity> reportsForChapter2 = Arrays.asList(
                new StudentReportEntity(course.getChapters().iterator().next(),
                        new PersonEntity(), new TimeSlotEntity(), "Report 3"),
                new StudentReportEntity(course.getChapters().iterator().next(),
                        new PersonEntity(), new TimeSlotEntity(), "Report 4")
        );

        when(courseService.get(courseSlug)).thenReturn(Optional.of((course)));
        when(studentReportRepository.findAllByChapterAndStateInOrderByTimeSlotId(
                course.getChapters().iterator().next(), StudentReportService.ACTUAL_STATES))
                .thenReturn(reportsForChapter1);
        when(studentReportRepository
                .findAllByChapterAndStateInOrderByTimeSlotId(course.getChapters()
                        .iterator().next(), StudentReportService.ACTUAL_STATES))
                .thenReturn(reportsForChapter2);

        List<List<StudentReportEntity>> result =
                studentReportService.getAllStudentsActualReports(courseSlug);
        assertEquals(reportsForChapter2, result.get(0));
    }

    @Test
    void testCreateStudentReportWithValidInput() {
        StudentReportCreationDto newStudentReportDto
                = new StudentReportCreationDto(1, 1L, "Test Report", 2L);
        PersonEntity student = new PersonEntity();
        ChapterEntity chapter = new ChapterEntity();
        TimeSlotEntity timeSlot = new TimeSlotEntity();

        when(chapterService.get(1L)).thenReturn(Optional.of(chapter));
        when(timeSlotRepository.findById(2L)).thenReturn(Optional.of(timeSlot));
        when(studentReportRepository.save(any()))
                .thenReturn(new StudentReportEntity(chapter, student, timeSlot, "Test Report"));

        Optional<StudentReportEntity> result = studentReportService
                .createStudentReport(Optional.of(student), newStudentReportDto);

        assertTrue(result.isPresent());
        assertEquals("Test Report", result.get().getTitle());
        assertEquals(chapter, result.get().getChapter());
        assertEquals(student, result.get().getStudent());
        assertEquals(timeSlot, result.get().getTimeSlot());
    }

    @Test
    void testCreateStudentReportWithInvalidInput() {
        StudentReportCreationDto newStudentReportDto = new StudentReportCreationDto(
                1, 1L, "Test Report", 2L);
        PersonEntity student = new PersonEntity();
        when(chapterService.get(1L)).thenReturn(Optional.empty());
        when(timeSlotRepository.findById(2L)).thenReturn(Optional.empty());
        Optional<StudentReportEntity> result = studentReportService
                .createStudentReport(Optional.of(student), newStudentReportDto);
        assertFalse(result.isPresent());
    }

    @Test
    void testChangeReportLikeList() {
        int reportId = 1;
        long studentId = 2;

        List<Long> initialLikedPersonsIdList = new ArrayList<>();
        initialLikedPersonsIdList.add(studentId);
        StudentReportEntity initialReport = new StudentReportEntity();
        initialReport.setId(reportId);
        initialReport.setLikedPersonsIdList(initialLikedPersonsIdList);

        when(studentReportRepository.findById(reportId)).thenReturn(Optional.of(initialReport));
        when(studentReportRepository.save(any())).thenReturn(initialReport);

        Optional<StudentReportEntity> result =
                studentReportService.changeReportLikeList(reportId, studentId);

        assertTrue(result.isPresent());
        StudentReportEntity updatedReport = result.get();
        List<Long> updatedLikedPersonsIdList = updatedReport.getLikedPersonsIdList();

        assertFalse(updatedLikedPersonsIdList.contains(studentId));

        result = studentReportService.changeReportLikeList(reportId, studentId);

        assertTrue(result.isPresent());
        updatedReport = result.get();
        updatedLikedPersonsIdList = updatedReport.getLikedPersonsIdList();
        assertTrue(updatedLikedPersonsIdList.contains(studentId));
    }
}