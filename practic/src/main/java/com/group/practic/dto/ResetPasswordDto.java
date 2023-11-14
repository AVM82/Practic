package com.group.practic.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record ResetPasswordDto(@NotBlank String code,
                               @NotBlank @Email String email,
                               @NotBlank @Min(value = 4) String newPassword) {
}