package com.group.practic.dto;

import com.group.practic.entity.ChapterPartEntity;
import com.group.practic.entity.StudentPracticeEntity;
import lombok.Getter;

@Getter
public class ChapterPartDto {

    ChapterPartEntity common;

    PracticeDto practice;


    public static ChapterPartDto map(ChapterPartEntity common, StudentPracticeEntity practice) {
        ChapterPartDto dto = new ChapterPartDto();
        dto.common = common;
        dto.practice = PracticeDto.map(practice);
        return dto;
    }

}
