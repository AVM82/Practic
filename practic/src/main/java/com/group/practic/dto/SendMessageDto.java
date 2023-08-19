package com.group.practic.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class SendMessageDto {
    @NotBlank
    @Min(5)
    @Email
    private String address;
    @NotBlank
    @Min(5)
    private String message;
    @NotBlank
    @Min(5)
    private String header;
}
