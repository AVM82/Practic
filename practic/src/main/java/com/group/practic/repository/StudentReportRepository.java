package com.group.practic.repository;

import com.group.practic.entity.ChapterEntity;
import com.group.practic.entity.StudentEntity;
import com.group.practic.entity.StudentReportEntity;
import com.group.practic.enumeration.ReportState;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface StudentReportRepository extends JpaRepository<StudentReportEntity, Long> {

    List<StudentReportEntity> findAllByChapterAndStateInOrderByTimeSlotId(ChapterEntity chapter,
            List<ReportState> states);

    long countByChapterAndStateIn(ChapterEntity chapter, List<ReportState> actualStates);

    long countByStudentAndChapterAndStateIn(StudentEntity student, ChapterEntity chapter,
            List<ReportState> actualStates);

}
