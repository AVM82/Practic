package com.group.practic.dto;

import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.Set;
import org.springframework.lang.NonNull;

public record CertificateDto(
        @NotEmpty
        String studentName,
        @NotEmpty
        String courseName,
        @NotEmpty
        Set<String> skills,
        @NotEmpty
        LocalDate start,
        LocalDate finish,
        int daysSpent
) {
}
