package com.group.practic.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


@Entity
@Table(name = "statistic_student_chapters")
@Getter
@Setter
public class StatisticStudentChapterEntity implements Serializable {

    private static final long serialVersionUID = -7054547394131869625L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    long id;

    @ManyToOne
    CourseEntity course;

    @ManyToOne
    ChapterEntity chapter;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = true)
    Timestamp updatedAt;

    long studentChapterCount;

    long dayCount;

    long reportCount;

    long quizPassedResult;

    long quizPassedCount;

    long quizTotalResult;

    long quizTotalCount;


    public StatisticStudentChapterEntity(CourseEntity course, ChapterEntity chapter) {
        this.course = course;
        this.chapter = chapter;
    }


    public void passData(StudentChapterEntity studentChapter) {
        studentChapterCount++;
        dayCount += studentChapter.daysSpent;
        reportCount += studentChapter.reportCount;
        // quiz statistic
    }

}
