package com.group.practic.entity;

import com.group.practic.enumeration.ReportState;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;


@Entity
@Table(name = "student_report")
public class StudentReportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    int id;

    @ManyToOne
    ChapterEntity chapter;

    @ManyToOne
    PersonEntity student;

    @Future
    LocalDateTime dateTime;

    @NotBlank
    String title;

    @Enumerated(EnumType.STRING)
    ReportState state = ReportState.ANNOUNCED;


    public StudentReportEntity() {
    }


    public StudentReportEntity(ChapterEntity chapter, PersonEntity student,
            @Future LocalDateTime dateTime, @NotBlank String title) {
        this.chapter = chapter;
        this.student = student;
        this.dateTime = dateTime;
        this.title = title;
    }


    public StudentReportEntity(int id, ChapterEntity chapter, PersonEntity student,
            @Future LocalDateTime dateTime, @NotBlank String title) {
        this(chapter, student, dateTime, title);
        this.id = id;
    }


    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
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


    public LocalDateTime getDateTime() {
        return dateTime;
    }


    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }


    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
    }


    public ReportState getState() {
        return state;
    }


    public void setState(ReportState state) {
        this.state = state;
    }

}
