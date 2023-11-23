package com.group.practic.dto;

import com.group.practic.entity.StudentPracticeEntity;
import lombok.Getter;


@Getter
public class PracticeDto {

    long id;

    int number;

    String state;


    public static PracticeDto map(StudentPracticeEntity entity) {
        if (entity == null) {
            return null;
        }
        PracticeDto dto = new PracticeDto();
        dto.id = entity.getId();
        dto.number = entity.getNumber();
        dto.state = entity.getState().name();
        return dto;
    }

}
