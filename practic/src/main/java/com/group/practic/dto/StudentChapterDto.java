package com.group.practic.dto;

import com.group.practic.entity.ChapterEntity;
import com.group.practic.entity.StudentChapterEntity;
import java.util.List;
import java.util.Optional;
import lombok.Getter;


@Getter
public class StudentChapterDto {

    long id;

    int number;

    String name;

    List<ChapterPartDto> parts;
    
    List<String> skills;

    String state;

    int reportCount;



    public static StudentChapterDto map(StudentChapterEntity entity) {
        StudentChapterDto dto = new StudentChapterDto();
        dto.id = entity.getId();
        dto.number = entity.getNumber();
        ChapterEntity chapter = entity.getChapter();
        dto.name = chapter.getName();
        dto.parts = chapter.getParts().stream().map(part -> ChapterPartDto.map(part,
                entity.getPracticeByNumber(part.getNumber()).get())).toList();
        dto.skills = chapter.getSkills();
        dto.state = entity.getState().name();
        dto.reportCount = entity.getReportCount();
        return dto;
    }


    public static Optional<StudentChapterDto> map(Optional<StudentChapterEntity> entity) {
        return entity.map(StudentChapterDto::map);
    }

}
