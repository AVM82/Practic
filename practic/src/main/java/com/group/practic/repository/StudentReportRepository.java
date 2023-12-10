package com.group.practic.repository;

import com.group.practic.entity.ChapterEntity;
import com.group.practic.entity.StudentReportEntity;
import com.group.practic.enumeration.ReportState;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface StudentReportRepository extends JpaRepository<StudentReportEntity, Long> {
    List<StudentReportEntity> findAllByStudentChapterChapterAndStateInOrderByTimeSlotId(
            ChapterEntity chapter, List<ReportState> states);

    long countByStudentChapterChapterAndStateIn(ChapterEntity chapter,
            List<ReportState> actualStates);

}
