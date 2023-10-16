package com.group.practic.dto;

import jakarta.validation.constraints.NotEmpty;


public record StudentReportCreationDto(Integer id, @NotEmpty Long chapter,
                                        @NotEmpty String title, @NotEmpty Long timeslotId) {

}
