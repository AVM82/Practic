package com.group.practic.dto;

import com.group.practic.entity.CourseEntity;
import com.group.practic.entity.MentorEntity;
import java.util.List;
import java.util.Set;
import lombok.Getter;


@Getter
public class CourseDto {

    long id;

    boolean inactive;

    Set<String> authors;

    List<CourseMentorDto> mentors;

    String courseType;

    String name;

    String description;

    String slug;

    String svg;

    boolean additionalMaterialsExist;


    public static CourseDto map(CourseEntity entity) {
        CourseDto dto = new CourseDto();
        dto.id = entity.getId();
        dto.inactive = entity.isInactive();
        dto.authors = entity.getAuthors();
        dto.mentors = entity.getMentors().stream().filter(MentorEntity::isActive)
                .map(CourseMentorDto::map).toList();
        dto.courseType = entity.getCourseType();
        dto.name = entity.getName();
        dto.description = entity.getDescription();
        dto.slug = entity.getSlug();
        dto.svg = entity.getSvg();
        dto.additionalMaterialsExist = entity.isAdditionalMaterialsExist();
        return dto;
    }

}
