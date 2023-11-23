package com.group.practic.service;

import com.group.practic.repository.AnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnswerService {

    private final AnswerRepository answerRepository;

    @Autowired
    public AnswerService(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    public List<Boolean> getAllById(List<Long> ids) {
        return ids.stream().sequential()
                .map(id -> id != 0 && answerRepository.findById(id).get().isCorrect()).toList();
    }
}
