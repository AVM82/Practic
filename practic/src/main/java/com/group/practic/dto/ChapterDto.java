package com.group.practic.dto;


public class ChapterDto {

    long id;

    int number;

    boolean visible = true;


    public ChapterDto() {}


    public ChapterDto(long id, int number, boolean visible) {
        this.id = id;
        this.number = number;
        this.visible = visible;
    }


    public long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public int getNumber() {
        return number;
    }


    public void setNumber(Integer number) {
        this.number = number;
    }


    public boolean isVisible() {
        return visible;
    }


    public void setVisible(boolean visible) {
        this.visible = visible;
    }

}
