package com.group.practic.dto;

import com.group.practic.entity.StateMentorEntity;
import lombok.Getter;

@Getter
public class MentorComplexDto {

    MentorDto mentorDto;

    StateMentorEntity stateMentor;


    public MentorComplexDto(MentorDto mentorDto, StateMentorEntity stateMentor) {
        this.mentorDto = mentorDto;
        this.stateMentor = stateMentor;
    }

}
