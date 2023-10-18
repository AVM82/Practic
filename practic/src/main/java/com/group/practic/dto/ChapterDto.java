package com.group.practic.dto;

import com.group.practic.entity.ChapterEntity;
import com.group.practic.entity.ChapterPartEntity;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;


@Getter
public class ChapterDto {

    long id;

    int number;

    String shortName;

    boolean visible;

    Set<Long> partsId = new HashSet<>();


    public ChapterDto() {}


    public ChapterDto(ChapterEntity chapter, boolean visible) {
        this.id = chapter.getId();
        this.number = chapter.getNumber();
        this.shortName = chapter.getShortName();
        this.visible = visible;
        chapter.getParts().forEach(part -> this.partsId.add(part.getId()));
    }


    public ChapterDto(long id, int number, String shortName, boolean visible,
            Set<ChapterPartEntity> parts) {
        this.id = id;
        this.number = number;
        this.shortName = shortName;
        this.visible = visible;
        parts.forEach(part -> this.partsId.add(part.getId()));
    }


    public void setVisible(boolean value) {
        this.visible = value;
    }

}
