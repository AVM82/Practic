package com.group.practic.entity;

import com.group.practic.enumeration.PracticeState;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "student_practice")
public class StudentPracticeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    ChapterEntity chapter;

    @ManyToOne
    PersonEntity student;

    @Enumerated(EnumType.STRING)
    PracticeState state = PracticeState.NOT_STARTED;

    public StudentPracticeEntity(ChapterEntity chapter, PersonEntity student,
                                 PracticeState state) {
        this.chapter = chapter;
        this.student = student;
        this.state = state;
    }

    public StudentPracticeEntity() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public ChapterEntity getChapter() {
        return chapter;
    }

    public void setChapter(ChapterEntity chapter) {
        this.chapter = chapter;
    }

    public PersonEntity getStudent() {
        return student;
    }

    public void setStudent(PersonEntity student) {
        this.student = student;
    }

    public PracticeState getState() {
        return state;
    }

    public void setState(PracticeState state) {
        this.state = state;
    }
}
