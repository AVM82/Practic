package com.group.practic.dto;

import com.group.practic.entity.GraduateEntity;
import lombok.Getter;

@Getter
public class GraduateDto {

    long id;

    String slug;


    public static GraduateDto map(GraduateEntity entity) {
        GraduateDto dto = new GraduateDto();
        dto.id = entity.getId();
        dto.slug = entity.getSlug();
        return dto;
    }

}
