package com.group.practic.dto;

import com.group.practic.entity.StudentChapterEntity;
import lombok.Getter;


@Getter
public class NewStateChapterDto {

    String state;

    int activeChapterNumber;


    public static NewStateChapterDto map(StudentChapterEntity chapter) {
        NewStateChapterDto dto = new NewStateChapterDto();
        dto.state = chapter.getState().name();
        dto.activeChapterNumber = chapter.getStudent().getActiveChapterNumber();
        return dto;
    }

}
