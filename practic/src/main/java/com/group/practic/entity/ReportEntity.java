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
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "reports")
@Getter
@Setter
public class ReportEntity implements Serializable {

    private static final long serialVersionUID = 2939396468588105108L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    long id;

    @ManyToOne
    StudentChapterEntity studentChapter;

    @ManyToOne
    CourseEntity course;

    @ManyToOne
    PersonEntity person;

    LocalDate date;
    
    int chapterNumber;

    @ManyToOne
    TopicReportEntity topic;

    @Enumerated(EnumType.STRING)
    ReportState state = ReportState.ANNOUNCED;
    
    LocalDateTime start;
    
    Duration duration;

    private Set<Long> likedPersonIds = new HashSet<>();


    public ReportEntity() {}


    public ReportEntity(CourseEntity course, PersonEntity person, LocalDate date,
            int chapterNumber, TopicReportEntity topic) {
        this.course = course;
        this.person = person;
        this.date = date;
        this.chapterNumber = chapterNumber;
        this.topic = topic;
    }


    public ReportEntity(StudentChapterEntity studentChapter, CourseEntity course,
            PersonEntity person, LocalDate date, TopicReportEntity topic) {
        this.studentChapter = studentChapter;
        this.course = course;
        this.person = person;
        this.date = date;
        this.chapterNumber = studentChapter.getNumber();
        this.topic = topic;
    }


    public boolean isCountable() {
        return state == ReportState.APPROVED;
    }


    public boolean isNonCancelled() {
        return state != ReportState.CANCELLED;
    }


    public boolean start() {
        boolean result = state.changeAllowed(ReportState.STARTED);
        if (result) {
            state = ReportState.STARTED;
            start = LocalDateTime.now();
        }
        return result;
    }
    
    
    public boolean finish() {
        boolean result = state.changeAllowed(ReportState.FINISHED);
        if (result) {
            state = ReportState.FINISHED;
            duration = Duration.between(start, LocalDateTime.now());
        }
        return result;
    }
    
}
