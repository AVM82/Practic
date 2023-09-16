package com.group.practic.service;

import com.group.practic.dto.NewStudentReportDto;
import com.group.practic.entity.ChapterEntity;
import com.group.practic.entity.CourseEntity;
import com.group.practic.entity.PersonEntity;
import com.group.practic.entity.StudentReportEntity;
import com.group.practic.enumeration.ReportState;
import com.group.practic.repository.StudentReportRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentReportService {

    private final StudentReportRepository studentReportRepository;
    private final CourseService courseService;
    private final ChapterService chapterService;
    static final List<ReportState> ACTUAL_STATES = List.of(ReportState.STARTED,
            ReportState.ANNOUNCED);

    @Autowired
    public StudentReportService(StudentReportRepository studentReportRepository,
            CourseService courseService, ChapterService chapterService) {
        this.studentReportRepository = studentReportRepository;
        this.courseService = courseService;
        this.chapterService = chapterService;
    }

    public List<List<StudentReportEntity>> getAllStudentsActualReports(String slug) {
        Optional<CourseEntity> course = courseService.get(slug);
        List<List<StudentReportEntity>> result = Collections.emptyList();
        if (course.isPresent()) {
            result = new ArrayList<>();
            for (ChapterEntity chapter : course.get().getChapters()) {
                result.add(
                        studentReportRepository.findAllByChapterAndStateIn(chapter, ACTUAL_STATES));
            }
        }
        return result;
    }

    public Optional<StudentReportEntity> createStudentReport(Optional<PersonEntity> student,
            NewStudentReportDto newStudentReportDto) {
        Optional<ChapterEntity> chapter = chapterService.get(newStudentReportDto.chapter());

        return (student.isPresent() && chapter.isPresent())
            ? Optional.ofNullable(studentReportRepository
            .save(new StudentReportEntity(chapter.get(), student.get(),
                newStudentReportDto.dateTime(), newStudentReportDto.title()))) : Optional.empty();
    }
}
