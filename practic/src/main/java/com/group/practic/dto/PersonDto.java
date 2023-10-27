package com.group.practic.dto;

import com.group.practic.entity.PersonEntity;
import com.group.practic.entity.RoleEntity;
import java.util.List;
import lombok.Getter;

@Getter
public class PersonDto {

    long id;

    boolean inactive;

    boolean ban;

    String name;

    String email;

    String discord;

    String linkedin;

    String profilePictureUrl;

    List<String> roles;

    List<StudentDto> students;

    List<MentorDto> mentors;

    List<ApplicantDto> applicants;


    PersonDto() {}


    public static PersonDto map(PersonEntity entity) {
        if (entity == null) {
            return null;
        }
        PersonDto dto = new PersonDto();
        dto.id = entity.getId();
        dto.name = entity.getName();
        dto.discord = entity.getDiscord();
        dto.email = entity.getEmail();
        dto.linkedin = entity.getLinkedin();
        dto.profilePictureUrl = entity.getProfilePictureUrl();
        dto.roles = entity.getRoles().stream().map(RoleEntity::getName).toList();
        dto.students = entity.getStudents().stream().map(StudentDto::map).toList();
        dto.mentors = entity.getMentors().stream().map(MentorDto::map).toList();
        dto.applicants = entity.getApplicants().stream().map(ApplicantDto::map).toList();
        return dto;
    }

}
