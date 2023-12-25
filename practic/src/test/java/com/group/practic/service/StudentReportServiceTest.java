package com.group.practic.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.group.practic.dto.StudentReportCreationDto;
import com.group.practic.entity.ChapterEntity;
import com.group.practic.entity.CourseEntity;
import com.group.practic.entity.PersonEntity;
import com.group.practic.entity.QuestionEntity;
import com.group.practic.entity.StudentChapterEntity;
import com.group.practic.entity.StudentEntity;
import java.util.ArrayList;
import java.util.Arrays;
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
    /*
     * @Mock private StudentReportRepository studentReportRepository;
     * 
     * @Mock private CourseService courseService;
     * 
     * 
     * @Mock private TimeSlotRepository timeSlotRepository;
     * 
     * @Mock private TimeSlotService timeSlotService;
     * 
     * @InjectMocks private StudentReportService studentReportService;
     * 
     * @BeforeEach void setUp() { }
     * 
     * @Test void testGetAllStudentsActualReports() { String courseSlug = "example-slug";
     * 
     * CourseEntity course = new CourseEntity(); ChapterEntity chapterEntity1 = new
     * ChapterEntity(1L, course, 1, "Chapter 1", new QuestionEntity().getQuiz()); ChapterEntity
     * chapterEntity2 = new ChapterEntity(2L, course, 2, "Chapter 2", new
     * QuestionEntity().getQuiz()); course.setChapters(Arrays.asList(chapterEntity1,
     * chapterEntity2));
     * 
     * StudentEntity student = new StudentEntity(new PersonEntity(), course); StudentReportEntity
     * studentReportEntity1 = new StudentReportEntity( new StudentChapterEntity(student,
     * chapterEntity1), new TimeSlotEntity(), "Report 1"); StudentReportEntity studentReportEntity2
     * = new StudentReportEntity( new StudentChapterEntity(student, chapterEntity1), new
     * TimeSlotEntity(), "Report 2");
     * 
     * List<StudentReportEntity> reportsForChapter1 = Arrays.asList(studentReportEntity1,
     * studentReportEntity2);
     * 
     * StudentReportEntity studentReportEntity3 = new StudentReportEntity( new
     * StudentChapterEntity(student, chapterEntity2), new TimeSlotEntity(), "Report 3");
     * StudentReportEntity studentReportEntity4 = new StudentReportEntity( new
     * StudentChapterEntity(student, chapterEntity2), new TimeSlotEntity(), "Report 4");
     * List<StudentReportEntity> reportsForChapter2 = Arrays.asList(studentReportEntity3,
     * studentReportEntity4);
     * 
     * when(courseService.get(courseSlug)).thenReturn(Optional.of((course)));
     * when(studentReportRepository.findAllByStudentChapterChapterAndStateInOrderByTimeSlotId(
     * chapterEntity1, StudentReportService.ACTUAL_STATES)) .thenReturn(reportsForChapter1);
     * when(studentReportRepository.findAllByStudentChapterChapterAndStateInOrderByTimeSlotId(
     * chapterEntity2, StudentReportService.ACTUAL_STATES)) .thenReturn(reportsForChapter2);
     * 
     * List<List<StudentReportEntity>> result =
     * studentReportService.getAllStudentsActualReports(courseSlug);
     * 
     * assertEquals(reportsForChapter1, result.get(0)); assertEquals(reportsForChapter2,
     * result.get(1)); }
     * 
     * @Test void testCreateStudentReportWithValidInput() { StudentReportCreationDto
     * newStudentReportDto = new StudentReportCreationDto(1, 1L, "Test Report", 2L);
     * StudentChapterEntity studentChapter = new StudentChapterEntity(); TimeSlotEntity timeSlot =
     * new TimeSlotEntity();
     * 
     * when(timeSlotRepository.findById(anyLong())).thenReturn(Optional.of(timeSlot));
     * when(timeSlotService.updateTimeSlotAvailability(anyLong(), anyBoolean()))
     * .thenReturn(Optional.of(timeSlot)); when(studentReportRepository.save(any())) .thenReturn(new
     * StudentReportEntity(studentChapter, timeSlot, "Test Report"));
     * 
     * StudentReportEntity result = studentReportService .createStudentReport(studentChapter,
     * newStudentReportDto);
     * 
     * assertNotNull(result); assertEquals("Test Report", result.getTitle());
     * assertEquals(studentChapter, result.getStudentChapter()); assertEquals(timeSlot,
     * result.getTimeSlot()); }
     * 
     * @Test void testCreateStudentReportWithInvalidInput() { StudentReportCreationDto
     * newStudentReportDto = new StudentReportCreationDto( 1, 1L, "Test Report", 2L);
     * StudentChapterEntity studentChapter = new StudentChapterEntity(); StudentReportEntity result
     * = studentReportService .createStudentReport(studentChapter, newStudentReportDto);
     * assertNull(result); }
     * 
     * @Test void testChangeReportLikeList() { long reportId = 1; long studentId = 2;
     * 
     * List<Long> initialLikedPersonsIdList = new ArrayList<>();
     * initialLikedPersonsIdList.add(studentId); StudentReportEntity initialReport = new
     * StudentReportEntity(); initialReport.setId(reportId);
     * initialReport.setLikedPersonsIdList(initialLikedPersonsIdList);
     * 
     * when(studentReportRepository.findById(reportId)).thenReturn(Optional.of(initialReport));
     * when(studentReportRepository.save(any())).thenReturn(initialReport);
     * 
     * Optional<StudentReportEntity> result = studentReportService.changeReportLikeList(reportId,
     * studentId);
     * 
     * assertTrue(result.isPresent()); StudentReportEntity updatedReport = result.get(); List<Long>
     * updatedLikedPersonsIdList = updatedReport.getLikedPersonsIdList();
     * 
     * assertFalse(updatedLikedPersonsIdList.contains(studentId));
     * 
     * result = studentReportService.changeReportLikeList(reportId, studentId);
     * 
     * assertTrue(result.isPresent()); updatedReport = result.get(); updatedLikedPersonsIdList =
     * updatedReport.getLikedPersonsIdList();
     * assertTrue(updatedLikedPersonsIdList.contains(studentId)); }
     */
}
