package com.group.practic.dto;

public class ChapterDto {



    long id;
    int number;

    String shortName;


    public ChapterDto() {
    }


    public ChapterDto(long id, int number, String shortName) {
        super();
        this.id = id;
        this.number = number;
        this.shortName = shortName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
