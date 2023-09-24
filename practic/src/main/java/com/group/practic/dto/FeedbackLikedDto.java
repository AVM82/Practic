package com.group.practic.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.Objects;

public class FeedbackLikedDto {

    @NotBlank
    private Long feedbackId;
    @NotBlank
    private Long personId;

    public FeedbackLikedDto(Long feedbackId, Long personId) {
        this.feedbackId = feedbackId;
        this.personId = personId;
    }

    public FeedbackLikedDto() {
    }

    public Long getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(Long feedbackId) {
        this.feedbackId = feedbackId;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FeedbackLikedDto that = (FeedbackLikedDto) o;
        return Objects.equals(feedbackId, that.feedbackId)
                && Objects.equals(personId, that.personId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(feedbackId, personId);
    }
}
