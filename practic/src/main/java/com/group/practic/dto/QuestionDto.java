package com.group.practic.dto;

import com.group.practic.entity.QuestionEntity;
import java.util.List;

public class QuestionDto {
    private Long id;
    private String question;
    private List<AnswerDto> answers;

    public static QuestionDto mapForUi(QuestionEntity entity) {
        QuestionDto dto = new QuestionDto();
        dto.setId(entity.getId());
        dto.setQuestion(entity.getQuestion());
        List<AnswerDto> answerDtos = entity.getAnswers().stream().map(AnswerDto::mapForUi).toList();
        dto.setAnswers(answerDtos);
        return dto;
    }
    public static QuestionDto map(QuestionEntity entity) {
        QuestionDto dto = new QuestionDto();
        dto.setId(entity.getId());
        dto.setQuestion(entity.getQuestion());
        List<AnswerDto> answerDtos = entity.getAnswers().stream().map(AnswerDto::map).toList();
        dto.setAnswers(answerDtos);
        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<AnswerDto> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AnswerDto> answers) {
        this.answers = answers;
    }
}
