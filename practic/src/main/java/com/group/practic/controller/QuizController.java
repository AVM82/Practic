package com.group.practic.controller;

import com.group.practic.dto.QuizDto;
import com.group.practic.service.QuizService;
import jakarta.validation.constraints.Min;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    private final QuizService quizService;

    @Autowired
    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping("/{quizId}/test")
    public ResponseEntity<QuizDto> getQuiz(@PathVariable(value = "quizId") Long quizId) {
        return ResponseEntity.ok(quizService.getQuiz(quizId));
    }

    @PostMapping("/{quizId}/test")
    public ResponseEntity<List<Boolean>> getResult(
            @Min(1) @PathVariable(value = "quizId") Long quizId,
            @RequestBody QuizDto quizzes) {
        return ResponseEntity.ok(quizService.getResult(quizId, quizzes));
    }
}
