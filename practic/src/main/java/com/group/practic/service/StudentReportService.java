package com.group.practic.service;

import com.group.practic.entity.ChapterEntity;
import com.group.practic.entity.CourseEntity;
import com.group.practic.entity.StudentReportEntity;
import com.group.practic.enumeration.ReportState;
import com.group.practic.repository.StudentReportRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentReportService {

    private final StudentReportRepository studentReportRepository;
    private final CourseService courseService;
    static final List<ReportState> ACTUAL_STATES = List.of(ReportState.STARTED,
            ReportState.ANNOUNCED);

    @Autowired
    public StudentReportService(StudentReportRepository studentReportRepository,
            CourseService courseService) {
        this.studentReportRepository = studentReportRepository;
        this.courseService = courseService;
    }


    public List<List<StudentReportEntity>> getAllStudentsActualReports(String slug) {
        Optional<CourseEntity> course = courseService.get(slug);
        List<List<StudentReportEntity>> result = null;
        if (course.isPresent()) {
            result = new ArrayList<>();
            for (ChapterEntity chapter : course.get().getChapters()) {
                result.add(
                        studentReportRepository.findAllByChapterAndStateIn(chapter, ACTUAL_STATES));
            }
        }
        return result;
    }
}
