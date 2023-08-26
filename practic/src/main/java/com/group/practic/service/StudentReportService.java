package com.group.practic.service;

import com.group.practic.entity.ChapterEntity;
import com.group.practic.entity.StudentReportEntity;
import com.group.practic.enumeration.ReportState;
import com.group.practic.repository.ChapterRepository;
import com.group.practic.repository.StudentReportRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentReportService {

    private final StudentReportRepository studentReportRepository;
    private final ChapterRepository chapterRepository;

    @Autowired
    public StudentReportService(StudentReportRepository studentReportRepository,
        ChapterRepository chapterRepository) {
        this.studentReportRepository = studentReportRepository;
        this.chapterRepository = chapterRepository;
    }


    public List<StudentReportEntity> getAllStudentsReportsByStateAndChapter(ReportState state,
        Long chapterId) {
        Optional<ChapterEntity> chapter = chapterRepository.findById(chapterId);
        return studentReportRepository.findByStateAndChapter(state, chapter);
    }
}
