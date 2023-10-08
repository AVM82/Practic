package com.group.practic.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.util.Objects;

public class TopicReportDTO {
    private long chapterId;

    @NotBlank
    @Min(value = 10)
    private String topicReport;

    public TopicReportDTO(int chapterId, String topicReport) {
        this.chapterId = chapterId;
        this.topicReport = topicReport;
    }

    public TopicReportDTO() {
    }

    public long getChapterId() {
        return chapterId;
    }

    public void setChapterId(long chapterId) {
        this.chapterId = chapterId;
    }

    public String getTopicReport() {
        return topicReport;
    }

    public void setTopicReport(String topicReport) {
        this.topicReport = topicReport;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TopicReportDTO that = (TopicReportDTO) o;
        return chapterId == that.chapterId && Objects.equals(topicReport, that.topicReport);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chapterId, topicReport);
    }
}
