package com.group.practic.dto;

import com.group.practic.entity.QuizEntity;
import java.util.List;

public class QuizDto {

    private Long id;
    private String quizName;
    private List<QuestionDto> questions;

    public static QuizDto map(QuizEntity entity) {
        QuizDto dto = new QuizDto();
        dto.setId(entity.getId());
        dto.setQuizName(entity.getChapter().getName());
        List<QuestionDto> questionDtos =
                entity.getQuestions().stream().map(QuestionDto::map).toList();
        dto.setQuestions(questionDtos);
        return dto;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuizName() {
        return quizName;
    }

    public void setQuizName(String quizName) {
        this.quizName = quizName;
    }

    public List<QuestionDto> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionDto> questions) {
        this.questions = questions;
    }
}
