package com.group.practic.dto;

import com.group.practic.entity.StudentPracticeEntity;
import java.sql.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;


@NoArgsConstructor
@Getter
@Setter
public class StudentPracticeDto {

    private String personName;

    @NotNull
    private long chapterPartId;

    private String chapterName;

    private String state;

    private Date updatedAt;

    @NotNull
    private long studentId;


    public static StudentPracticeDto map(StudentPracticeEntity practice) {
        StudentPracticeDto dto = new StudentPracticeDto();
        dto.studentId = practice.getStudent().getId();
        dto.personName = practice.getStudent().getName();
        dto.chapterPartId = practice.getChapterPart().getId();
        dto.chapterName = practice.getChapterPart().getPraxisPurpose();
        dto.state = practice.getState().name();
        return dto;
    }

}
