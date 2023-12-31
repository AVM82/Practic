package com.group.practic.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.group.practic.entity.AnswerEntity;
import com.group.practic.entity.QuestionEntity;
import com.group.practic.entity.QuizEntity;
import com.group.practic.entity.QuizResultEntity;
import com.group.practic.repository.QuizRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@Slf4j
class QuizServiceTest {
    @InjectMocks
    private QuizService quizService;
    @Mock
    private QuizRepository quizRepository;
    @Mock
    private AnswerService answerService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetQuiz() {
        Long quizId = 1L;
        QuizEntity quizEntity = new QuizEntity();
        quizEntity.setId(quizId);
        QuestionEntity question1 = new QuestionEntity();
        question1.setQuestion("Question 1");
        List<AnswerEntity> answers1 = new ArrayList<>();
        AnswerEntity answer1 = new AnswerEntity();
        answer1.setAnswer("Answer 1");
        answer1.setCorrect(true);
        answers1.add(answer1);
        question1.setAnswers(answers1);

        List<QuestionEntity> questions = new ArrayList<>();
        questions.add(question1);
        quizEntity.setQuestions(questions);
        when(quizRepository.findById(quizId)).thenReturn(Optional.of(quizEntity));
        QuizEntity result = quizService.get(quizId).get();

        assertNotNull(result);
        assertEquals(quizId, result.getId());
        assertFalse(result.getQuestions().isEmpty());

    }
}
