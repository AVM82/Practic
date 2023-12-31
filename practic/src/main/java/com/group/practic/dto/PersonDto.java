package com.group.practic.dto;

import com.group.practic.entity.MentorEntity;
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

    String profilePictureUrl;

    String personPageUrl;

    List<String> roles;

    List<PersonStudentDto> students;

    List<GraduateDto> graduates;

    List<PersonMentorDto> mentors;

    List<PersonApplicantDto> applicants;


    public static PersonDto map(PersonEntity entity) {
        if (entity == null) {
            return null;
        }
        PersonDto dto = new PersonDto();
        dto.id = entity.getId();
        dto.inactive = entity.isInactive();
        dto.ban = entity.isBan();
        dto.name = entity.getName();
        dto.discord = entity.getDiscord();
        dto.email = entity.getEmail();
        dto.profilePictureUrl = entity.getProfilePictureUrl();
        dto.personPageUrl = entity.getPersonPageUrl();
        dto.roles = entity.getRoles().stream().map(RoleEntity::getName).toList();
        dto.students = entity.getStudents().stream()
                .filter(student -> student.getFinish() == null && !student.isBan())
                .map(PersonStudentDto::map).toList();
        dto.mentors = entity.getMentors().stream().filter(MentorEntity::isActive)
                .map(PersonMentorDto::map).toList();
        dto.applicants = entity.getApplicants().stream()
                .filter(applicant -> !applicant.isApplied() && !applicant.isRejected())
                .map(PersonApplicantDto::map).toList();
        dto.graduates = entity.getGraduates().stream().map(GraduateDto::map).toList();
        return dto;
    }


    public static List<PersonDto> map(List<PersonEntity> entities) {
        return entities.stream().map(PersonDto::map).toList();
    }

}
