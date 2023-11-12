package com.group.practic.dto;

import com.group.practic.entity.ChapterPartEntity;
import com.group.practic.entity.StudentChapterEntity;
import java.util.List;
import java.util.Optional;
import lombok.Getter;


@Getter
public class StudentChapterDto {

    long id;

    int number;

    String name;

    List<ChapterPartEntity> parts;

    String state;

    int reportCount;

    List<StudentPracticeDto> practices;


    public static StudentChapterDto map(StudentChapterEntity entity) {
        StudentChapterDto dto = new StudentChapterDto();
        dto.id = entity.getId();
        dto.number = entity.getNumber();
        dto.name = entity.getChapter().getName();
        dto.parts = entity.getChapter().getParts();
        dto.state = entity.getState().name();
        dto.reportCount = entity.getReportCount();
        dto.practices = entity.getPractices().stream().map(StudentPracticeDto::map).toList();
        return dto;
    }


    public static Optional<StudentChapterDto> map(Optional<StudentChapterEntity> entity) {
        return entity.map(StudentChapterDto::map);
    }

}
