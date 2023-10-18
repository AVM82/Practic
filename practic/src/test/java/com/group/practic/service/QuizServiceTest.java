package com.group.practic.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.group.practic.dto.AnswerDto;
import com.group.practic.dto.QuestionDto;
import com.group.practic.dto.QuizDto;
import com.group.practic.entity.AnswerEntity;
import com.group.practic.entity.QuestionEntity;
import com.group.practic.entity.QuizEntity;
import com.group.practic.repository.QuizRepository;
import java.util.ArrayList;
import java.util.Collections;
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
        QuizDto result = quizService.getQuiz(quizId);

        assertNotNull(result);
        assertEquals(quizId, result.getId());
        assertFalse(result.getQuestions().isEmpty());

    }

    @Test
    void testGetResult() {

        QuestionEntity questionEntity1 = new QuestionEntity();
        questionEntity1.setQuestion("Question 1");
        AnswerEntity answerEntity1 = new AnswerEntity();
        answerEntity1.setAnswer("Answer 1");
        answerEntity1.setCorrect(true);
        questionEntity1.setAnswers(Collections.singletonList(answerEntity1));

        QuizEntity quizEntity = new QuizEntity();
        quizEntity.setQuestions(Collections.singletonList(questionEntity1));

        QuestionDto questionDto1 = new QuestionDto();
        questionDto1.setQuestion("Question 1");
        AnswerDto answerDto1 = new AnswerDto();
        answerDto1.setAnswer("Answer 1");
        answerDto1.setCorrect(true);
        questionDto1.setAnswers(Collections.singletonList(answerDto1));

        QuizDto quizDto = new QuizDto();
        quizDto.setQuestions(Collections.singletonList(questionDto1));
        Long quizId = 1L;
        when(quizRepository.findById(quizId)).thenReturn(Optional.of(quizEntity));
        List<Boolean> result = quizService.getResult(quizId, quizDto);
        assertEquals(1, result.size());
        assertTrue(result.get(0));
    }
}