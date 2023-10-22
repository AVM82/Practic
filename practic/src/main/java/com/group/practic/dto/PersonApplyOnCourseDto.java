package com.group.practic.dto;

import com.group.practic.entity.PersonApplicationEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonApplyOnCourseDto {

    private long id;

    private String name;

    private String profilePictureUrl;

    private String courseSlug;

    
    public static PersonApplyOnCourseDto map(PersonApplicationEntity personApplication) {
        PersonApplyOnCourseDto dto = new PersonApplyOnCourseDto();
        dto.id = personApplication.getPerson().getId();
        dto.name = personApplication.getPerson().getName();
        dto.profilePictureUrl = personApplication.getPerson().getProfilePictureUrl();
        dto.courseSlug = personApplication.getCourse().getSlug();
        return dto;
    }
}
