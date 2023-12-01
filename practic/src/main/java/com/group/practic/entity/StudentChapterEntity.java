package com.group.practic.entity;

import com.group.practic.enumeration.ChapterState;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


@Table(name = "student_chapters")
@Getter
@Setter
@Entity
public class StudentChapterEntity implements Serializable, DaysCountable<ChapterState> {

    private static final long serialVersionUID = 7733498302034511375L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    long id;

    @ManyToOne
    StudentEntity student;

    int number;

    @OneToOne
    ChapterEntity chapter;

    @Enumerated(EnumType.STRING)
    ChapterState state = ChapterState.NOT_STARTED;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = true)
    Timestamp updatedAt;

    @OneToMany(mappedBy = "studentChapter", cascade = CascadeType.MERGE)
    @OrderBy("number")
    private List<StudentPracticeEntity> practices = new ArrayList<>();

    @OneToMany(mappedBy = "studentChapter", cascade = CascadeType.MERGE)
    @OrderBy("number")
    private List<StudentReportEntity> reports = new ArrayList<>();

    int daysSpent;

    LocalDate startCounting;


    public StudentChapterEntity() {}


    public StudentChapterEntity(StudentEntity student, ChapterEntity chapter) {
        this.student = student;
        this.chapter = chapter;
        this.number = chapter.number;
    }


    public Optional<StudentPracticeEntity> getPracticeByNumber(int number) {
        return practices.stream().filter(prac -> prac.number == number).findFirst();
    }

}
