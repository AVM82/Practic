package com.group.practic.dto;

import com.group.practic.entity.StudentEntity;
import java.util.Optional;

public class StudentDto {

    long id;

    boolean inactive;

    boolean ban;
    
    String registered;

    String slug;

    int activeChapterNumber;


    public static StudentDto map(StudentEntity entity) {
        if (entity == null) {
            return null;
        }
        StudentDto dto = new StudentDto();
        dto.id = entity.getId();
        dto.inactive = entity.isInactive();
        dto.ban = entity.isBan();
        dto.registered = entity.getRegistered().toString();
        dto.slug = entity.getCourse().getSlug();
        dto.activeChapterNumber = entity.getActiveChapterNumber();
        return dto;
    }


    public static Optional<StudentDto> map(Optional<StudentEntity> entity) {
        return entity.map(StudentDto::map);
    }

}
