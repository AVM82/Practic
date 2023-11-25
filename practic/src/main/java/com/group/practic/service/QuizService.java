package com.group.practic.service;

import com.group.practic.entity.QuizEntity;
import com.group.practic.repository.QuizRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuizService {
    private final QuizRepository quizRepository;
    private final AnswerService answerService;

    @Autowired
    public QuizService(QuizRepository quizRepository, AnswerService answerService) {
        this.quizRepository = quizRepository;
        this.answerService = answerService;
    }

    public Optional<QuizEntity> get(Long id) {
        return quizRepository.findById(id);
    }

    public List<Boolean> getResult(List<Long> ids) {
        return answerService.getAllById(ids);
    }
}
