package com.group.practic.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "state_students")
@NoArgsConstructor
@Getter
@Setter
public class StateStudentEntity
        implements PersonStateEntityChangeable<StudentEntity, StateStudentEntity> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    long id;

    long studentId;

    boolean inactive;

    boolean ban;

    String slug;

    int activeChapterNumber;


    public StateStudentEntity(StudentEntity entity) {
        this.studentId = entity.id;
        this.inactive = entity.inactive;
        this.ban = entity.ban;
        this.slug = entity.getCourse().getSlug();
        this.activeChapterNumber = entity.getActiveChapterNumber();
    }


    @Override
    public StateStudentEntity update(StudentEntity entity) {
        this.inactive = entity.inactive;
        this.ban = entity.ban;
        this.activeChapterNumber = entity.getActiveChapterNumber();
        return this;
    }


    @Override
    public boolean match(StudentEntity entity) {
        return this.studentId == entity.id;
    }


    public static boolean shouldBeDeleted(StudentEntity entity) {
        return entity.getId() == 0;
    }

}
