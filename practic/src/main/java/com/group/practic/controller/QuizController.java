package com.group.practic.controller;

import static com.group.practic.util.ResponseUtils.badRequest;
import static com.group.practic.util.ResponseUtils.getResponse;

import com.group.practic.dto.QuizDto;
import com.group.practic.entity.QuizEntity;
import com.group.practic.entity.QuizResultEntity;
import com.group.practic.service.QuizService;
import com.group.practic.service.StudentService;
import jakarta.validation.constraints.Min;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
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
    private final StudentService studentService;

    @Autowired
    public QuizController(QuizService quizService,
                          StudentService studentService) {
        this.quizService = quizService;
        this.studentService = studentService;
    }

    @GetMapping("/{quizId}")
    public ResponseEntity<QuizDto> getQuizForUi(
            @PathVariable(value = "quizId") Long quizId) {
        return getResponse(quizService.get(quizId).map(QuizDto::mapForUi));
    }

    @GetMapping("/{quizId}/origin")
    public ResponseEntity<QuizDto> getOriginQuiz(
            @PathVariable(value = "quizId") Long quizId) {
        return getResponse(quizService.get(quizId).map(QuizDto::map));
    }

    @GetMapping("/start/{studentChapterId}")
    public ResponseEntity<Long> getQuizResultId(
            @PathVariable(value = "studentChapterId") Long studentChapterId) {
        return getResponse(studentService.getStudentChapter(studentChapterId)
                .map(quizService::createQuizResult));
    }

    @GetMapping("/{quizId}/result/{quizResultId}")
    public ResponseEntity<Collection<List<Long>>> loadAnswers(
            @PathVariable(value = "quizId") Long quizId,
            @PathVariable(value = "quizResultId") Long quizResultId) {
        Optional<QuizEntity> quiz = quizService.get(quizId);
        Optional<QuizResultEntity> quizResult = quizService.getQuizResult(quizResultId);
        return getResponse(quizService.loadAnswers(quiz.get(), quizResult.get()));
    }

    @PostMapping("/{quizId}/{quizResultId}/{time}")
    public ResponseEntity<Collection<Boolean>> getResult(
            @Min(1) @PathVariable(value = "quizId") Long quizId,
            @PathVariable(value = "quizResultId") Long quizResultId,
            @PathVariable(value = "time") Long time,
            @RequestBody List<List<Long>> ids) {
        Optional<QuizEntity> quiz = quizService.get(quizId);
        Optional<QuizResultEntity> quizResult = quizService.getQuizResult(quizResultId);
        return quiz.isEmpty() || quizResult.isEmpty() ? badRequest()
                : getResponse(quizService.getResult(quiz.get(), quizResult.get(), ids, time));
    }
}
