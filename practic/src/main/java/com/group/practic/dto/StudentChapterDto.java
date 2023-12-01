package com.group.practic.dto;

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

    String state;

    int reportCount;



    public static StudentChapterDto map(StudentChapterEntity entity) {
        StudentChapterDto dto = new StudentChapterDto();
        dto.id = entity.getId();
        dto.number = entity.getNumber();
        dto.name = entity.getChapter().getName();
        dto.parts = entity.getChapter().getParts().stream().map(part -> ChapterPartDto.map(part,
                entity.getPracticeByNumber(part.getNumber()).get())).toList();
        dto.state = entity.getState().name();
        //dto.reportCount = entity.getReportCount();
        return dto;
    }


    public static Optional<StudentChapterDto> map(Optional<StudentChapterEntity> entity) {
        return entity.map(StudentChapterDto::map);
    }

}
