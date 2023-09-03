package com.group.practic.dto;

public class ReferenceTitleDto {

    String reference;

    String title;


    public ReferenceTitleDto() {
    }


    public ReferenceTitleDto(String reference, String title) {
        super();
        this.reference = reference;
        this.title = title;
    }


    public String getReference() {
        return reference;
    }


    public void setReference(String reference) {
        this.reference = reference;
    }


    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
    }

}
