package com.group.practic.dto;

import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.time.LocalTime;

public record NewStudentReportDto(@NotEmpty Long chapter, @NotEmpty String title,
                                  @NotEmpty LocalDate date, @NotEmpty LocalTime time, @NotEmpty Long timeslotId) {

}
