package com.group.practic.service;

import com.group.practic.entity.AnswerResultEntity;
import com.group.practic.entity.QuestionEntity;
import com.group.practic.entity.QuizEntity;
import com.group.practic.entity.QuizResultEntity;
import com.group.practic.entity.StudentChapterEntity;
import com.group.practic.repository.AnswerResultRepository;
import com.group.practic.repository.QuizRepository;
import com.group.practic.repository.QuizResultRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class QuizService {

    public static final int CORRECT_ANSWERS_PERCENT = 75;
    private final QuizRepository quizRepository;
    private final AnswerService answerService;
    private final StudentService studentService;
    private final QuizResultRepository resultRepository;
    private final AnswerResultRepository answerResultRepository;

    @Autowired
    public QuizService(QuizRepository quizRepository, AnswerService answerService,
                       StudentService studentService,
                       QuizResultRepository resultRepository,
                       AnswerResultRepository answerResultRepository) {
        this.quizRepository = quizRepository;
        this.answerService = answerService;
        this.studentService = studentService;
        this.resultRepository = resultRepository;
        this.answerResultRepository = answerResultRepository;
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
//        if (passedTest) {
//            studentService.quizPassed(quizResult.getStudentChapter());
//        }
        saveAnswers(quiz, quizResult, ids);
    }

    private void saveAnswers(QuizEntity quiz, QuizResultEntity quizResult,
                             List<List<Long>> answerIds) {
        AtomicInteger i = new AtomicInteger();
        quiz.getQuestions().forEach(question -> {
            AnswerResultEntity answerResult = new AnswerResultEntity();
            answerResult.setQuestion(question);
            answerResult.setQuizResult(quizResult);
            answerResult.setAnswerIds(answerIds.get(i.getAndIncrement()));
            answerResultRepository.save(answerResult);
        });
    }

    public List<List<Long>> loadAnswers(QuizEntity quiz, QuizResultEntity quizResult) {
        List<List<Long>> result = new ArrayList<>();
        List<QuestionEntity> questions = quiz.getQuestions();
        for (QuestionEntity question : questions) {
            result.add(answerResultRepository
                    .findByQuizResultIdAndQuestionId(quizResult.getId(), question.getId()));
        }
        return result;
    }

    public Long createQuizResult(StudentChapterEntity studentChapter) {
        return resultRepository.save(
                new QuizResultEntity(studentChapter, new ArrayList<>())).getId();
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
