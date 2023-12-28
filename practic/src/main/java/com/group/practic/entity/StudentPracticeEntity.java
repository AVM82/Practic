package com.group.practic.entity;

import com.group.practic.enumeration.PracticeState;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


@Entity
@Table(name = "practices")
@Setter
@Getter
public class StudentPracticeEntity implements Serializable, DaysCountable<PracticeState> {

    private static final long serialVersionUID = -4520682618456683847L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    long id;

    @ManyToOne
    StudentChapterEntity studentChapter;

    int number;

    @Enumerated(EnumType.STRING)
    PracticeState state = PracticeState.NOT_STARTED;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = true)
    Timestamp updatedAt;

    @Column(name = "days_spent")
    int daysSpent;

    LocalDate startCounting;


    public StudentPracticeEntity(StudentChapterEntity studentChapter, int number) {
        this.studentChapter = studentChapter;
        this.number = number;
    }


    public StudentPracticeEntity() {}

}
