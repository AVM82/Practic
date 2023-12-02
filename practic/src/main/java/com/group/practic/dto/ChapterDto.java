package com.group.practic.dto;

import com.group.practic.entity.ChapterEntity;
import java.util.List;
import lombok.Getter;


@Getter
public class ChapterDto {

    long id;

    int number;

    String name;

    List<ChapterPartDto> parts;
    
    List<String> skills;

    int reportCount;


    public static ChapterDto map(ChapterEntity entity, int reportCount) {
        ChapterDto dto = new ChapterDto();
        dto.id = entity.getId();
        dto.number = entity.getNumber();
        dto.name = entity.getName();
        dto.parts = entity.getParts().stream().map(part -> ChapterPartDto.map(part, null)).toList();
        dto.skills = entity.getSkills();
        dto.reportCount = reportCount;
        return dto;
    }


    public static ChapterDto map(ChapterEntity entity) {
        return map(entity, 0);
    }

}
