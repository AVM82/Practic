package com.group.practic.dto;

import com.group.practic.entity.StudentEntity;

public class StudentDto {

    long id;
    
    boolean inactive;

    boolean ban;
    
    String slug;
    
    int activeChapterNumber;
    
    
    public static StudentDto map(StudentEntity entity) {
        StudentDto dto = new StudentDto();
        dto.id = entity.getId();
        dto.inactive = entity.isInactive();
        dto.ban = entity.isBan();
        dto.slug = entity.getCourse().getSlug();
        dto.activeChapterNumber = entity.getActiveChapterNumber();
        return dto;
    }
    
}
