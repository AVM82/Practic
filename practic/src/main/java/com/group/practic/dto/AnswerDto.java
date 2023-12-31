package com.group.practic.dto;


import com.group.practic.entity.AnswerEntity;

public class AnswerDto {

    private Long id;
    private String answer;
    private boolean correct;

    public AnswerDto() {
    }

    public AnswerDto(Long id, String answer, boolean correct) {
        this.id = id;
        this.answer = answer;
        this.correct = correct;
    }

    public static AnswerDto mapForUi(AnswerEntity entity) {
        return new AnswerDto(entity.getId(), entity.getAnswer(), false);
    }

    public static AnswerDto map(AnswerEntity entity) {
        return new AnswerDto(entity.getId(), entity.getAnswer(), entity.isCorrect());
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }
}
