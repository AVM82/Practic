package com.group.practic.dto;

import com.group.practic.entity.TopicReportEntity;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.util.Objects;

public class TopicReportDto {
    private long chapterId;

    @NotBlank
    @Min(value = 10)
    private String topicReport;

    public TopicReportDto(TopicReportEntity topicReportEntity) {
        this.chapterId = topicReportEntity.getId();
        this.topicReport =topicReportEntity.getTopic();
    }

    public TopicReportDto() {
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
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TopicReportDto that = (TopicReportDto) o;
        return chapterId == that.chapterId && Objects.equals(topicReport, that.topicReport);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chapterId, topicReport);
    }
}
