package com.group.practic.repository;

import com.group.practic.entity.CourseEntity;
import com.group.practic.entity.ReportEntity;
import com.group.practic.enumeration.ReportState;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface ReportRepository extends JpaRepository<ReportEntity, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE ReportEntity report SET report.state='CANCELLED' "
            + "WHERE report.state='ANNOUNCED' AND report.date < :datenow")
    int cancelMissedAnnouncedReport(LocalDate datenow);


    List<ReportEntity> 
            findAllByCourseAndDateGreaterThanEqualAndStateNotAndChapterNumberIsOrderByDateAsc(
                    CourseEntity course, LocalDate date, ReportState cancelled, int number);


    List<ReportEntity> 
            findAllByCourseAndDateGreaterThanEqualAndStateNotOrderByChapterNumberAscDateAsc(
                    CourseEntity course, LocalDate date, ReportState cancelled);


    long countByCourseAndDateEqualsAndStateNot(CourseEntity course, LocalDate date,
            ReportState cancelled);

}
