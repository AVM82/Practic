package com.group.practic.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDateTime;
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
    @Pattern(regexp = "^[A-Za-z0-9.,-]*$", message = "Only letters, numbers, and . , - characters are allowed")
    private String subjectReport;
    @NotBlank
    @Min(5)
    @Pattern(regexp = "^[A-Za-z0-9.,-]*$", message = "Only letters, numbers, and . , - characters are allowed")
    private String description;
}
