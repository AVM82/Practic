package com.group.practic.dto;

import com.group.practic.entity.CourseEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseDto {

    private String slug;

    private String name;

    private String svg;


    public CourseDto() {}


    public CourseDto(String name, String svg, String slug) {
        this.name = name;
        this.svg = svg;
        this.slug = slug;
    }


    public void setSlug(String slug) {
        this.slug = slug;
    }


    public static CourseDto map(CourseEntity course) {
        CourseDto dto = new CourseDto();
        dto.slug = course.getSlug();
        dto.name = course.getName();
        dto.svg = course.getSvg();
        return dto;
    }


}
