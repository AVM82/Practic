package com.group.practic.dto;

import com.group.practic.entity.StudentPracticeEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PracticeDto {

    long id;

    @NotNull
    private long chapterPartId;

    @NotNull
    private String state;


    public static PracticeDto map(StudentPracticeEntity entity) {
        PracticeDto dto = new PracticeDto();
        dto.id = entity.getId();
        dto.chapterPartId = entity.getChapterPartId();
        dto.state = entity.getState().name();
        return dto;
    }

}
