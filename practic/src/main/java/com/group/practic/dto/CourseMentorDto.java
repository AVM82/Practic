package com.group.practic.dto;

import com.group.practic.entity.MentorEntity;
import lombok.Getter;

@Getter
public class CourseMentorDto {

    long id;

    boolean inactive;

    String name;

    String personPageUrl;


    public static CourseMentorDto map(MentorEntity entity) {
        if (entity == null) {
            return null;
        }
        CourseMentorDto dto = new CourseMentorDto();
        dto.id = entity.getId();
        dto.inactive = entity.isInactive();
        dto.name = entity.getPerson().getName();
        dto.personPageUrl = entity.getPersonPageUrl();
        return dto;
    }

}
