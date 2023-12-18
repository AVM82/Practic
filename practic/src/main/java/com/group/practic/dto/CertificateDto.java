package com.group.practic.dto;

import java.time.LocalDate;
import java.util.Set;

public record CertificateDto(
        String studentName,
        String courseName,
        Set<String> skills,
        LocalDate start,
        LocalDate finish,
        int daysSpent
) {
}
