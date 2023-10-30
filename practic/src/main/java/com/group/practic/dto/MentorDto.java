package com.group.practic.dto;

import com.group.practic.entity.MentorEntity;
import lombok.Getter;

@Getter
public class MentorDto {

    long id;

    String name;

    String slug;

    String linkedInUrl;


    public static MentorDto map(MentorEntity entity) {
        MentorDto dto = new MentorDto();
        dto.id = entity.getId();
        dto.name = entity.getPerson().getName();
        dto.slug = entity.getCourse().getSlug();
        dto.linkedInUrl = entity.getLinkedInUrl();
        return dto;
    }

}
