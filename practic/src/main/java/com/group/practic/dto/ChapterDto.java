package com.group.practic.dto;

public class ChapterDto {

    int number;

    String shortName;


    public ChapterDto() {
    }


    public ChapterDto(int number, String shortName) {
        super();
        this.number = number;
        this.shortName = shortName;
    }


    public int getNumber() {
        return number;
    }


    public void setNumber(int number) {
        this.number = number;
    }


    public String getShortName() {
        return shortName;
    }


    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

}
