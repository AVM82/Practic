package com.group.practic.dto;

import java.util.Objects;


public final class CourseDto {

    private String name;

    private String description;

    private String shortName;


    public CourseDto() {
    }


    CourseDto(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public CourseDto(String name, String description, String shortName) {
        this.name = name;
        this.description = description;
        this.shortName = shortName;
    }

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        CourseDto that = (CourseDto) obj;
        return this == that || (Objects.equals(this.name, that.name)
                && Objects.equals(this.description, that.description)
                && Objects.equals(this.shortName, that.shortName));
    }


    @Override
    public int hashCode() {
        return Objects.hash(name, description, shortName);
    }


    @Override
    public String toString() {
        return "CourseDto[" + "name=" + name + ", " + "description=" + description
                + ", " + "shortName=" + shortName + ']';
    }

}
