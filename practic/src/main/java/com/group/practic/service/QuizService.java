package com.group.practic.service;

import static com.group.practic.util.Converter.toDto;

import com.group.practic.dto.AnswerDto;
import com.group.practic.dto.QuestionDto;
import com.group.practic.dto.QuizDto;
import com.group.practic.entity.AnswerEntity;
import com.group.practic.entity.QuestionEntity;
import com.group.practic.entity.QuizEntity;
import com.group.practic.exception.ResourceNotFoundException;
import com.group.practic.repository.QuizRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuizService {
    private final QuizRepository quizRepository;

    @Autowired
    public QuizService(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    public QuizDto getQuiz(Long quizId) {
        Optional<QuizEntity> optional = quizRepository.findById(quizId);
        if (optional.isPresent()) {
            QuizEntity quizEntity = optional.get();
            return toDto(quizEntity);
        }
        throw new ResourceNotFoundException("getQuiz", "quizId", quizId);
    }

    public List<Boolean> getResult(Long quizId, QuizDto quizDtos) {
        Optional<QuizEntity> optional = quizRepository.findById(quizId);
        if (optional.isPresent()) {
            QuizEntity quizEntity = optional.get();
            List<QuestionEntity> questionsFromDb = quizEntity.getQuestions();

            return questionsFromDb.stream()
                    .map(questionEntity -> {
                        String question = questionEntity.getQuestion();
                        List<AnswerEntity> answers = questionEntity.getAnswers();
                        Optional<QuestionDto> matchingQuestionDto = quizDtos.getQuestions().stream()
                                .filter(questionDto -> question.equals(questionDto.getQuestion()))
                                .findFirst();

                        return matchingQuestionDto
                                .map(questionDto -> checkAnswer(questionDto.getAnswers(), answers))
                                .orElse(false);
                    })
                    .toList();
        }
        throw new ResourceNotFoundException("getQuiz", "quizId", quizId);
    }

    private boolean checkAnswer(List<AnswerDto> fromUi, List<AnswerEntity> fromDb) {
        return fromDb.stream().allMatch(dbAnswer ->
                fromUi.stream()
                        .filter(uiAnswer -> uiAnswer.getAnswer().equals(dbAnswer.getAnswer()))
                        .allMatch(uiAnswer -> uiAnswer.isCorrect() == dbAnswer.isCorrect())
        );
    }
}
