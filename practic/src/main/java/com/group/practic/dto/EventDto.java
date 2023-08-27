package com.group.practic.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDateTime;


public class EventDto {

    @NotBlank
    private LocalDateTime startEvent;

    @NotBlank
    private LocalDateTime endEvent;

    @NotBlank
    @Min(5)
    @Pattern(regexp = "^[A-Za-z0-9.,-]*$")
    private String subjectReport;

    @NotBlank
    @Min(5)
    @Pattern(regexp = "^[A-Za-z0-9.,-]*$")
    private String description;


    public EventDto() {
    }


    public EventDto(@NotBlank LocalDateTime startEvent, @NotBlank LocalDateTime endEvent,
            @NotBlank @Min(5) @Pattern(regexp = "^[A-Za-z0-9.,-]*$") String subjectReport,
            @NotBlank @Min(5) @Pattern(regexp = "^[A-Za-z0-9.,-]*$") String description) {
        super();
        this.startEvent = startEvent;
        this.endEvent = endEvent;
        this.subjectReport = subjectReport;
        this.description = description;
    }


    public LocalDateTime getStartEvent() {
        return startEvent;
    }


    public void setStartEvent(LocalDateTime startEvent) {
        this.startEvent = startEvent;
    }


    public LocalDateTime getEndEvent() {
        return endEvent;
    }


    public void setEndEvent(LocalDateTime endEvent) {
        this.endEvent = endEvent;
    }


    public String getSubjectReport() {
        return subjectReport;
    }


    public void setSubjectReport(String subjectReport) {
        this.subjectReport = subjectReport;
    }


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }

}
