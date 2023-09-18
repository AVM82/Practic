package com.group.practic.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.util.Objects;

public class FeedbackDto {

    private int id;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Min(5)
    private String feedback;

    private int likes;

    public FeedbackDto(String email, String feedback) {
        this.email = email;
        this.feedback = feedback;
    }

    public FeedbackDto() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FeedbackDto that = (FeedbackDto) o;
        return id == that.id && likes == that.likes &&
                Objects.equals(email, that.email) && Objects.equals(feedback, that.feedback);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, feedback, likes);
    }
}
