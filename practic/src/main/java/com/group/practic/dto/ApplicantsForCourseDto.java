package com.group.practic.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class ApplicantsForCourseDto {

    String courseName;

    List<ApplicantDto> applicants;


    public ApplicantsForCourseDto(String courseName, List<ApplicantDto> applicants) {
        this.courseName = courseName;
        this.applicants = applicants;
    }

}
