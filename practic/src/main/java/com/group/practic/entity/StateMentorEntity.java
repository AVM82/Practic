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
@Table(name = "state_mentors")
@NoArgsConstructor
@Getter
@Setter
public class StateMentorEntity
        implements PersonStateEntityChangeable<MentorEntity, StateMentorEntity> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    long id;

    long mentorId;

    String slug;

    String linkedInUrl;


    public StateMentorEntity(MentorEntity entity) {
        this.mentorId = entity.id;
        this.slug = entity.getCourse().slug;
        this.linkedInUrl = entity.linkedInUrl;
    }


    @Override
    public StateMentorEntity update(MentorEntity entity) {
        this.slug = entity.getCourse().slug;
        this.linkedInUrl = entity.linkedInUrl;
        return this;
    }


    @Override
    public boolean match(MentorEntity entity) {
        return this.mentorId == entity.id;
    }


    public static boolean shouldBeDeleted(MentorEntity entity) {
        return entity.inactive;
    }

}
