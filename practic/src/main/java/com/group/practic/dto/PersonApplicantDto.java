package com.group.practic.dto;

import com.group.practic.entity.ApplicantEntity;
import lombok.Getter;


@Getter
public class PersonApplicantDto {

    long applicantId;

    String slug;


    public static PersonApplicantDto map(ApplicantEntity entity) {
        PersonApplicantDto dto = new PersonApplicantDto();
        dto.applicantId = entity.getId();
        dto.slug = entity.getCourse().getSlug();
        return dto;
    }

}
