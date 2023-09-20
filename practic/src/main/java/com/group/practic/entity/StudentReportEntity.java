package com.group.practic.entity;

import com.group.practic.enumeration.ReportState;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "student_report")
public class StudentReportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    long id;

    @ManyToOne
    ChapterEntity chapter;

    @ManyToOne
    PersonEntity student;

    @OneToOne
    @NotNull
    TimeSlotEntity timeSlot;

    @NotBlank
    String title;

    @Enumerated(EnumType.STRING)
    ReportState state = ReportState.ANNOUNCED;


    List<Long> likedPersonsIdList = new ArrayList<>();


    public StudentReportEntity() {
    }


    public StudentReportEntity(ChapterEntity chapter, PersonEntity student,
                               TimeSlotEntity timeslot, @NotBlank String title) {
        this.chapter = chapter;
        this.student = student;
        this.timeSlot = timeslot;
        this.title = title;

    }


    public StudentReportEntity(long id, ChapterEntity chapter, PersonEntity student,
            TimeSlotEntity timeslot, @NotBlank String title) {
        this(chapter, student, timeslot, title);
        this.id = id;
    }


    public long getId() {
        return id;
    }


    public void setId(long id) {
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

    public TimeSlotEntity getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(TimeSlotEntity timeSlot) {
        this.timeSlot = timeSlot;
    }

    public List<Long> getLikedPersonsIdList() {
        return likedPersonsIdList;
    }

    public void setLikedPersonsIdList(List<Long> likedPersonsIdList) {
        this.likedPersonsIdList = likedPersonsIdList;
    }
}
