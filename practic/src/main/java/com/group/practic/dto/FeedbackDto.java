package com.group.practic.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeedbackDto {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Min(5)
    private String feedback;


    public FeedbackDto() {}

}
