package com.group.practic.dto;

import com.group.practic.entity.ChapterPartEntity;
import java.util.Set;
import java.util.stream.Collectors;

public class ChapterDto {

    long id;

    int number;

    String shortName;

    boolean visible;


    Set<Long> chapterPartIds;


    public ChapterDto() {}


    public ChapterDto(int number, String shortName, boolean visible, boolean active) {
        this.number = number;
        this.shortName = shortName;
        this.visible = visible;
    }


    public int getNumber() {
        return number;
    }


    public void setNumber(Integer number) {
        this.number = number;
    }


    public String getShortName() {
        return shortName;
    }


    public void setShortName(String shortName) {
        this.shortName = shortName;
    }


    public long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public boolean isVisible() {
        return visible;
    }


    public void setVisible(boolean visible) {
        this.visible = visible;
    }


    public Set<Long> getChapterPartIds() {
        return chapterPartIds;
    }


    public void setChapterPartIds(Set<ChapterPartEntity> chapterParts) {
        if (!chapterParts.isEmpty()) {
            this.chapterPartIds =
                    chapterParts.stream().map(ChapterPartEntity::getId).collect(Collectors.toSet());
        }

    }

}
