package com.group.practic.service;

import com.group.practic.entity.QuizEntity;
import com.group.practic.entity.QuizResultEntity;
import com.group.practic.entity.StudentChapterEntity;
import com.group.practic.repository.QuizRepository;
import com.group.practic.repository.QuizResultRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class QuizService {

    public static final int CORRECT_ANSWERS_PERCENT = 75;
    private final QuizRepository quizRepository;
    private final AnswerService answerService;
    private final StudentService studentService;
    private final QuizResultRepository resultRepository;

    @Autowired
    public QuizService(QuizRepository quizRepository, AnswerService answerService,
                       StudentService studentService, QuizResultRepository resultRepository) {
        this.quizRepository = quizRepository;
        this.answerService = answerService;
        this.studentService = studentService;
        this.resultRepository = resultRepository;
    }

    public Optional<QuizEntity> get(Long id) {
        return quizRepository.findById(id);
    }

    public List<Boolean> getResult(
            QuizEntity quiz, QuizResultEntity quizResult,
            List<List<Long>> ids, long time) {
        List<Long> fromDb = answerService.getAllCorrectByQuiz(quiz.getId());
        List<Boolean> result = checkQuiz(ids, fromDb);

        saveResult(result, quiz, quizResult, ids, time);

        return result;
    }

    private void saveResult(List<Boolean> result, QuizEntity quiz,
                            QuizResultEntity quizResult,
                            List<List<Long>> ids, long time) {
        quizResult.setQuestionCount(quiz.getQuestions().size());
        int countOfNotZeros = (int) ids.stream()
                .flatMap(List::stream)
                .filter(i -> i != 0L)
                .count();
        quizResult.setAnsweredCount(countOfNotZeros);
        int correctAnswers = (int) result.stream().filter(b -> b).count();
        quizResult.setCorrectAnsweredCount(correctAnswers);
        boolean passedTest = (
                (correctAnswers * 100) / quiz.getQuestions().size()) >= CORRECT_ANSWERS_PERCENT;
        quizResult.setPassed(passedTest);
        quizResult.setSecondSpent(time);
        resultRepository.save(quizResult);
        if (passedTest) {
            studentService.quizPassed(quizResult.getStudentChapter());
        }
    }

    public Long createQuizResult(StudentChapterEntity studentChapter) {
        return resultRepository.save(new QuizResultEntity(studentChapter)).getId();
    }

    public Optional<QuizResultEntity> getQuizResult(Long quizResultId) {
        return resultRepository.findById(quizResultId);
    }

    private List<Boolean> checkQuiz(List<List<Long>> fromUi, List<Long> fromDb) {
        return fromUi.stream()
                .map(fromDb::containsAll)
                .toList();
    }
}
