package com.group.practic.dto;

import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

public record NewStudentReportDto(@NotEmpty Long chapter, @NotEmpty String title,
                                      @NotEmpty LocalDateTime dateTime) {

}
