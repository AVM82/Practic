package com.group.practic.dto;

import java.util.Objects;


public class PersonDto {

    private String name;

    private String discord;


    PersonDto() {
    }


    PersonDto(String name, String discord) {
        this.name = name;
        this.discord = discord;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getDiscord() {
        return discord;
    }


    public void setDiscord(String discord) {
        this.discord = discord;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        PersonDto that = (PersonDto) obj;
        return this == that || (Objects.equals(this.name, that.name)
                && Objects.equals(this.discord, that.discord));
    }


    @Override
    public int hashCode() {
        return Objects.hash(name, discord);
    }

}
