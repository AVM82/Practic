package com.group.practic.dto;

import com.group.practic.entity.ApplicantEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicantDto {

    long id;

    String name;

    String slug;

    boolean isApplied;


    public static ApplicantDto map(ApplicantEntity entity) {
        ApplicantDto dto = new ApplicantDto();
        dto.id = entity.getId();
        dto.name = entity.getPerson().getName();
        dto.slug = entity.getCourse().getSlug();
        dto.isApplied = entity.isApplied();
        return dto;
    }

}
