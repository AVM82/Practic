package com.group.practic.dto;

import com.group.practic.entity.ChapterPartEntity;
import com.group.practic.entity.QuizEntity;
import com.group.practic.entity.StudentChapterEntity;
import jakarta.persistence.OrderBy;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.Getter;


@Getter
public class StudentChapterDto {

    long id;

    int number;

    String name;

    @OrderBy("number")
    Set<ChapterPartEntity> parts;

    QuizEntity quiz;

    String state;

    int reportCount;

    List<StudentPracticeDto> practices;


    public static StudentChapterDto map(StudentChapterEntity entity) {
        StudentChapterDto dto = new StudentChapterDto();
        dto.id = entity.getId();
        dto.number = entity.getNumber();
        dto.parts = entity.getChapter().getParts();
        dto.quiz = entity.getChapter().getQuiz();
        dto.state = entity.getState().name();
        dto.reportCount = entity.getReportCount();
       // dto.practices = entity.getPractices().stream().map(StudentPracticeDto::map).toList();
        return dto;
    }


    public static Optional<StudentChapterDto> map(Optional<StudentChapterEntity> entity) {
        return entity.map(StudentChapterDto::map);
    }

}
