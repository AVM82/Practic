package com.group.practic.dto;


public final class CourseDto {

    private String slug;

    private String name;

    private String svg;


    public CourseDto() {}


    public CourseDto(String name, String svg, String slug) {
        super();
        this.name = name;
        this.svg = svg;
        this.slug = slug;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getSvg() {
        return svg;
    }


    public void setSvg(String svg) {
        this.svg = svg;
    }


    public String getSlug() {
        return slug;
    }


    public void setSlug(String slug) {
        this.slug = slug;
    }


}
