package com.group.practic.dto;

import com.group.practic.entity.AnswerResultEntity;
import java.util.List;

public class AnswerResultDto {
    private Long id;
    private List<Long> answerIds;

    public AnswerResultDto() {
    }

    public AnswerResultDto(Long id, List<Long> answerIds) {
        this.id = id;
        this.answerIds = answerIds;
    }

    public static AnswerResultDto map(AnswerResultEntity entity) {
        return new AnswerResultDto(entity.getId(), entity.getAnswerIds());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Long> getAnswerIds() {
        return answerIds;
    }

    public void setAnswerIds(List<Long> answerIds) {
        this.answerIds = answerIds;
    }
}
