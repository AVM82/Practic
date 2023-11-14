package com.group.practic.dto;

import com.group.practic.entity.PersonEntity;
import com.group.practic.entity.RoleEntity;
import jakarta.transaction.Transactional;
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

    String profilePictureUrl;

    String personPageUrl;

    List<String> roles;

    List<PersonStudentDto> students;

    List<PersonMentorDto> mentors;

    List<PersonApplicantDto> applicants;


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
        dto.personPageUrl = entity.getPersonPageUrl();
        dto.roles = entity.getRoles().stream().map(RoleEntity::getName).toList();
        dto.students = entity.getStudents().stream().map(PersonStudentDto::map).toList();
        dto.mentors = entity.getMentors().stream().map(PersonMentorDto::map).toList();
        dto.applicants = entity.getApplicants().stream()
                .filter(applicant -> !applicant.isApplied() && !applicant.isRejected())
                .map(PersonApplicantDto::map).toList();
        return dto;
    }


    public static List<PersonDto> map(List<PersonEntity> entities) {
        return entities.stream().map(PersonDto::map).toList();
    }

}
