package com.group.practic.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeedbackDto {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 5)
    private String feedback;

}
