package com.group.practic.dto;

import com.group.practic.entity.CourseEntity;
import com.group.practic.entity.PersonEntity;
import com.group.practic.entity.RoleEntity;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class PersonDto {

    long id;

    String name;

    String discord;

    String email;

    String linkedin;

    String profilePictureUrl;

    Set<String> roles;

    Set<String> courses;


    PersonDto() {}


    public static PersonDto map(PersonEntity person) {
        PersonDto personDto = new PersonDto();
        personDto.id = person.getId();
        personDto.name = person.getName();
        personDto.discord = person.getDiscord();
        personDto.email = person.getEmail();
        personDto.linkedin = person.getLinkedin();
        personDto.profilePictureUrl = person.getProfilePictureUrl();
        personDto.roles = person.getRoles().stream()
                .map(RoleEntity::getName).collect(Collectors.toSet());
        personDto.courses = person.getCourses().stream()
                .map(CourseEntity::getSlug).collect(Collectors.toSet());
        return personDto;
    }

}
