package com.group.practic.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class ApplicantsForCourseDto {

    String slug;

    List<ApplicantDto> applicants;


    public ApplicantsForCourseDto(String slug, List<ApplicantDto> applicants) {
        this.slug = slug;
        this.applicants = applicants;
    }

}