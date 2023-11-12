package com.group.practic.dto;

import com.group.practic.entity.StudentEntity;
import lombok.Getter;


@Getter
public class PersonStudentDto {

    long id;

    boolean inactive;

    boolean ban;

    String slug;

    int activeChapterNumber;


    public static PersonStudentDto map(StudentEntity entity) {
        if (entity == null) {
            return null;
        }
        PersonStudentDto dto = new PersonStudentDto();
        dto.id = entity.getId();
        dto.inactive = entity.isInactive();
        dto.ban = entity.isBan();
        dto.slug = entity.getCourse().getSlug();
        dto.activeChapterNumber = entity.getActiveChapterNumber();
        return dto;
    }

}
