package com.group.practic.dto;

import com.group.practic.entity.PersonEntity;
import com.group.practic.entity.RoleEntity;
import com.group.practic.entity.StateApplicantEntity;
import com.group.practic.entity.StateMentorEntity;
import com.group.practic.entity.StateStudentEntity;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Set;
import lombok.Getter;

@Getter
public class PersonDto {

    long id;

    boolean inactive;

    boolean ban;

    String name;

    String email;

    String discord;

    String profilePictureUrl;

    List<String> roles;

    Set<StateStudentEntity> students;

    Set<StateMentorEntity> mentors;

    Set<StateApplicantEntity> applicants;


    PersonDto() {}


    @Transactional
    public static PersonDto map(PersonEntity entity) {
        if (entity == null) {
            return null;
        }
        PersonDto dto = new PersonDto();
        dto.id = entity.getId();
        dto.name = entity.getName();
        dto.discord = entity.getDiscord();
        dto.email = entity.getEmail();
        dto.profilePictureUrl = entity.getProfilePictureUrl();
        dto.roles = entity.getRoles().stream().map(RoleEntity::getName).toList();
        dto.students = entity.getStudentStates();
        dto.mentors = entity.getMentorStates();
        dto.applicants = entity.getApplicantStates();
        return dto;
    }


    public static List<PersonDto> map(List<PersonEntity> entities) {
        return entities.stream().map(PersonDto::map).toList();
    }

}
