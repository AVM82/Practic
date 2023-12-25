package com.group.practic.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


@Entity
@Table(name = "statistic_course")
@Getter
@Setter
public class StatisticCourseEntity implements Serializable {

    private static final long serialVersionUID = -1067745861450866068L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    long id;

    @OneToOne
    CourseEntity course;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = true)
    Timestamp updatedAt;

    long applicantTotalCount;

    long applicantAppliedCount;

    long applicantRejectedCount;

    long studentStartedCount;

    long studentFinishedCount;

    long studentBannedCount;

    long graduateCount;


}
