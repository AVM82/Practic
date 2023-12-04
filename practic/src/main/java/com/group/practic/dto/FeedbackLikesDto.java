package com.group.practic.dto;

import com.group.practic.entity.FeedbackEntity;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class FeedbackLikesDto {

    FeedbackPageDto page;
    FeedbackDto feedback;

    public FeedbackLikesDto(FeedbackEntity entity) {
        this.feedback = FeedbackDto.map(entity);
    }

    public FeedbackLikesDto(FeedbackPageDto page) {
        this.page = page;

    }
}
