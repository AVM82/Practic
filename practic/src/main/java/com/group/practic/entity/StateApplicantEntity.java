package com.group.practic.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "state_applicants")
@NoArgsConstructor
@Getter
@Setter
public class StateApplicantEntity
        implements PersonStateEntityChangeable<ApplicantEntity, StateApplicantEntity>,
                Serializable {

    private static final long serialVersionUID = -6823774274250234435L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    long id;

    long applicantId;

    String slug;


    public StateApplicantEntity(ApplicantEntity entity) {
        this.applicantId = entity.id;
        this.slug = entity.getCourse().getSlug();
    }


    @Override
    public StateApplicantEntity update(ApplicantEntity entity) {
        return this;
    }


    @Override
    public boolean match(ApplicantEntity entity) {
        return this.applicantId == entity.id;
    }


    public static boolean shouldBeDeleted(ApplicantEntity entity) {
        return entity.isRejected || entity.isApplied;
    }

}
