package com.group.practic.dto;

import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.time.LocalTime;

public record StudentReportCreationDto( Integer id, @NotEmpty Long chapter,
                                        @NotEmpty String title, @NotEmpty Long timeslotId) {

}
