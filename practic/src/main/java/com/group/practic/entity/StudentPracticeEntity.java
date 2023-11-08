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
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "practices")
@Setter
@Getter
public class StudentPracticeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
/*
    @ManyToOne
    StudentChapterEntity studentChapter;
*/    
    @ManyToOne
    private ChapterPartEntity chapterPart;

    @Enumerated(EnumType.STRING)
    private PracticeState state = PracticeState.NOT_STARTED;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;


    public StudentPracticeEntity(ChapterPartEntity chapterPart) {
        this.chapterPart = chapterPart;
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
