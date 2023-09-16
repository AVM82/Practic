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
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "student_practice")
@Getter
@Setter
public class StudentPracticeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    ChapterPartEntity chapterPart;

    @ManyToOne
    PersonEntity student;

    @ManyToOne
    ChapterEntity chapter;

    @Enumerated(EnumType.STRING)
    PracticeState state = PracticeState.NOT_STARTED;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;

    public StudentPracticeEntity(ChapterPartEntity chapter, PersonEntity student,
                                 PracticeState state) {
        this.chapterPart = chapter;
        this.student = student;
        this.state = state;
    }

    public StudentPracticeEntity() {
    }
}
