package com.group.practic.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group.practic.dto.FeedbackDto;
import com.group.practic.entity.ChapterEntity;
import com.group.practic.entity.FeedbackEntity;
import com.group.practic.entity.PersonEntity;
import com.group.practic.enumeration.FeedbackSortState;
import com.group.practic.repository.CourseRepository;
import com.group.practic.security.jwt.TokenProvider;
import com.group.practic.service.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j

@WebMvcTest(FeedbackController.class)
@AutoConfigureMockMvc
public class FeedbackControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @InjectMocks
    private FeedbackController feedbackController;
    @MockBean
    private FeedbackService feedbackService;
    @MockBean
    private PersonService personService;

    @MockBean
    private TokenProvider tokenProvider;
    @Mock
    private Authentication authentication;

    @BeforeEach
    public void setup() {
        // Ініціалізуємо mock-об'єкти перед кожним тестом
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testGetAllFeedbacks() throws Exception {
        FeedbackEntity feedback1 = new FeedbackEntity();
        feedback1.setId(1L);
        feedback1.setStudent(new PersonEntity());
        feedback1.setFeedback("Це перший відгук");
        feedback1.setLikes(5);

        FeedbackEntity feedback2 = new FeedbackEntity();
        feedback2.setId(2L);
        feedback2.setStudent(new PersonEntity());
        feedback2.setFeedback("Це другий відгук");
        feedback2.setLikes(10);

        List<FeedbackEntity> feedbacks = Arrays.asList(feedback1, feedback2);
        when(feedbackService.getAllFeedbacks(FeedbackSortState.DATE_DESCENDING)).thenReturn(feedbacks);
        assertEquals(feedbackService.getAllFeedbacks(FeedbackSortState.DATE_DESCENDING).size(), 2);
        ResponseEntity<Collection<FeedbackEntity>> response =
                feedbackController.getAllFeedbacks(FeedbackSortState.DATE_DESCENDING);
        assertEquals(response.getBody().size(), 2);
        int like = response.getBody().stream().findFirst().map(FeedbackEntity::getLikes).orElse(0);
        assertEquals(like, 5);
        mockMvc.perform(get("/api/feedbacks/")
                        .param("feedbackSort", "DATE_DESCENDING")
                        .contentType(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.TEXT_HTML));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testAddFeedback() throws Exception {
        FeedbackDto feedbackDto = new FeedbackDto();
        feedbackDto.setEmail("testuser@example.com");
        feedbackDto.setFeedback("New feedback");

        FeedbackEntity savedFeedback = new FeedbackEntity();
        savedFeedback.setId(1L);
        savedFeedback.setStudent(new PersonEntity());
        savedFeedback.setFeedback("New feedback");
        savedFeedback.setLikes(3);
        when(feedbackService.addFeedback(feedbackDto)).thenReturn(savedFeedback);
        ResponseEntity<FeedbackEntity> responseEntity = feedbackController.addFeedback(feedbackDto);
        assertEquals("3", String.valueOf(responseEntity.getBody().getLikes()));
        assertEquals("New feedback", responseEntity.getBody().getFeedback());
        verify(feedbackService, times(1)).addFeedback(feedbackDto);
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testIncrementLike() throws Exception {
        FeedbackEntity feedback = new FeedbackEntity();
        feedback.setId(1L);
        feedback.setStudent(new PersonEntity());
        feedback.setFeedback("more likes");
        feedback.setLikes(5);
        Long idFeedback = 1L;
        Long idPerson = 2L;
        when(feedbackService.incrementLikeAndSavePerson(eq(idFeedback), eq(idPerson)))
                .thenAnswer((InvocationOnMock invocation) -> {
                    feedback.setLikes(feedback.getLikes() + 1);
                    return feedback;
                });
        ResponseEntity<FeedbackEntity> response = feedbackController.incrementLike(idFeedback, idPerson);
        verify(feedbackService, times(1)).incrementLikeAndSavePerson(idFeedback, idPerson);
        assertEquals(String.valueOf(response.getBody().getLikes()), "6");
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(feedback, response.getBody());
    }
//
//    @Test
//    public void testDecrementLike() throws Exception {
//        FeedbackLikedDto dto = new FeedbackLikedDto();
//        // Заповніть об'єкт dto з необхідними даними
//
//        FeedbackEntity feedbackEntity = new FeedbackEntity();
//        // Заповніть feedbackEntity з очікуваним результатом зменшення лайків
//
//        when(feedbackService.decrementLikeAndRemovePerson(any(Long.class), any(Long.class))).thenReturn(feedbackEntity);
//
//        mockMvc.perform(patch("/api/feedbacks/")
//                        .content(asJsonString(dto))
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
//        // Додайте додаткові перевірки JSON відповіді, якщо потрібно
//    }
//
//    @Test
//    public void testDeleteFeedback() throws Exception {
//        Long feedbackId = 1L;
//        FeedbackEntity feedbackEntity = new FeedbackEntity();
//        // Заповніть feedbackEntity з очікуваним результатом видалення відгуку
//
//        when(feedbackService.deleteFeedback(any(Long.class))).thenReturn(feedbackEntity);
//
//        mockMvc.perform(delete("/api/feedbacks/")
//                        .param("idFeedback", String.valueOf(feedbackId))
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
//        // Додайте додаткові перевірки JSON відповіді, якщо потрібно
//    }
//
//    // Допоміжний метод для конвертації об'єкта у JSON-рядок
//    private String asJsonString(Object obj) throws Exception {
//        ObjectMapper objectMapper = new ObjectMapper();
//        return objectMapper.writeValueAsString(obj);
//    }
}