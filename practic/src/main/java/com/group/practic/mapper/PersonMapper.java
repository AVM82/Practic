package com.group.practic.mapper;

import com.group.practic.dto.PersonDto;
import com.group.practic.entity.CourseEntity;
import com.group.practic.entity.PersonEntity;
import com.group.practic.entity.RoleEntity;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PersonMapper {

    public static final PersonMapper INSTANCE = Mappers.getMapper(PersonMapper.class);


    Set<String> mapRoles(Set<RoleEntity> roles);

    Set<String> mapCourses(Set<CourseEntity> roles);

    PersonDto map(PersonEntity person);


    default String map(RoleEntity role) {
        return role.getName();
    }


    default String map(CourseEntity course) {
        return course.getSlug();
    }

}
