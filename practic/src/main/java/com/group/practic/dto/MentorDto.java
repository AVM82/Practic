package com.group.practic.dto;

import com.group.practic.entity.MentorEntity;
import lombok.Getter;


@Getter
public class MentorDto {

    long id;

    boolean inactive;

    String name;

    String slug;

    String personPageUrl;


    public static MentorDto map(MentorEntity entity) {
        MentorDto dto = new MentorDto();
        dto.id = entity.getId();
        dto.inactive = entity.isInactive();
        dto.name = entity.getPerson().getName();
        dto.slug = entity.getCourse().getSlug();
        dto.personPageUrl = entity.getPersonPageUrl();
        return dto;
    }



}
