package com.group.practic.controller;

import static com.group.practic.util.ResponseUtils.getResponse;

import com.group.practic.dto.QuizDto;
import com.group.practic.service.QuizService;
import jakarta.validation.constraints.Min;
import java.util.Collection;
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

    @GetMapping("/{quizId}")
    public ResponseEntity<QuizDto> getQuiz(@PathVariable(value = "quizId") Long quizId) {
        return getResponse(quizService.get(quizId).map(QuizDto::map));
    }

    @PostMapping("/{quizId}")
    public ResponseEntity<Collection<Boolean>> getResult(
            @Min(1) @PathVariable(value = "quizId") Long quizId,
            @RequestBody List<Long> ids) {
        //TODO: use quizId for statistic
        return getResponse(quizService.getResult(ids));
    }
}
