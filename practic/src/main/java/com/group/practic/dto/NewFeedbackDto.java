package com.group.practic.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewFeedbackDto {

    @Size(min = 5)
    private String feedback;

}
