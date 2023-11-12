package com.group.practic.dto;

import jakarta.validation.constraints.NotEmpty;


public record StudentReportCreationDto(long id, @NotEmpty long chapterId,
                                        @NotEmpty String title, @NotEmpty Long timeslotId) {
}