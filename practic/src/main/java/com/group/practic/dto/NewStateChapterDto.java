package com.group.practic.dto;

import com.group.practic.entity.StudentChapterEntity;
import lombok.Getter;


@Getter
public class NewStateChapterDto {

    String state;

    int activeChapterNumber;


    public NewStateChapterDto(StudentChapterEntity chapter) {
        this.state = chapter.getState().name();
        this.activeChapterNumber = chapter.getStudent().getActiveChapterNumber();
    }

}
