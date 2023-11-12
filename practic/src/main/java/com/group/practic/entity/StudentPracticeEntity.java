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
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "practices")
@Setter
@Getter
public class StudentPracticeEntity implements Serializable {

    private static final long serialVersionUID = -4520682618456683847L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    long id;

    @ManyToOne
    StudentChapterEntity studentChapter;

    long chapterPartId;

    @Enumerated(EnumType.STRING)
    PracticeState state = PracticeState.NOT_STARTED;

    LocalDateTime createdAt = LocalDateTime.now();

    LocalDateTime updatedAt;


    public StudentPracticeEntity(StudentChapterEntity studentChapter, long chapterPartId) {
        this.studentChapter = studentChapter;
        this.chapterPartId = chapterPartId;
    }


    public StudentPracticeEntity() {}


    public boolean setNewState(PracticeState newState) {
        if (state.changeAllowed(newState)) {
            state = newState;
            updatedAt = LocalDateTime.now();
            return true;
        }
        return false;
    }

}
