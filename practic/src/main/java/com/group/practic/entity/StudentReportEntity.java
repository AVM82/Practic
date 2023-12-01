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
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "student_reports")
@Getter
@Setter
public class StudentReportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    long id;

    @ManyToOne
    StudentChapterEntity studentChapter;
 

    //int number;

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


    public StudentReportEntity(StudentChapterEntity studentChapter, TimeSlotEntity timeslot,
                               @NotBlank String title/*, int number*/) {
        this.studentChapter = studentChapter;
        this.timeSlot = timeslot;
        this.title = title;
        // this.number = number;

    }



}

