package com.group.practic.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonApplyOnCourseDto {
    private String id;
    private String name;
    private String profilePictureUrl;
    private String courseSlug;
}
