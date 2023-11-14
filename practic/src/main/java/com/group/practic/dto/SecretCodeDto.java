package com.group.practic.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SecretCodeDto(@NotBlank String code, @NotBlank @Email String email) {
}