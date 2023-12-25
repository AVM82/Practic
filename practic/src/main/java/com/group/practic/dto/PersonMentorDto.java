package com.group.practic.dto;

import com.group.practic.entity.MentorEntity;
import lombok.Getter;


@Getter
public class PersonMentorDto {

    long mentorId;

    boolean inactive;

    String slug;


    public static PersonMentorDto map(MentorEntity entity) {
        PersonMentorDto dto = new PersonMentorDto();
        dto.mentorId = entity.getId();
        dto.inactive = entity.isInactive();
        dto.slug = entity.getCourse().getSlug();
        return dto;
    }

}
