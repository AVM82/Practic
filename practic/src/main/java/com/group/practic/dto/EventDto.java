package com.group.practic.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Locale;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class EventDto {
    @NotBlank
    private LocalDateTime startEvent;
    @NotBlank
    private LocalDateTime endEvent;
    @NotBlank
    @Min(5)
    private String subjectReport;
    @NotBlank
    @Min(5)
    private String description;

    public String getSubjectReport() {
        String[] result = subjectReport.split("");
        result[0] = result[0].toLowerCase(Locale.ROOT);
        return String.join("", result);
    }
}
