package com.group.practic.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.group.practic.dto.FeedbackDto;
import com.group.practic.entity.FeedbackEntity;
import com.group.practic.entity.PersonEntity;
import com.group.practic.enumeration.FeedbackSortState;
import com.group.practic.repository.FeedbackRepository;
import com.group.practic.repository.PersonRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class FeedbackServiceTest {

    @InjectMocks
    private FeedbackService feedbackService;

    @Mock
    private FeedbackRepository feedbackRepository;

    @Mock
    private PersonRepository personRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllFeedbacksDateAscending() {
        FeedbackEntity feedback1 = new FeedbackEntity();
        feedback1.setDateTime(LocalDateTime.of(2023, 4, 1, 10, 0));
        FeedbackEntity feedback2 = new FeedbackEntity();
        feedback2.setDateTime(LocalDateTime.of(2023, 4, 2, 11, 0));

        when(feedbackRepository.findAll()).thenReturn(List.of(feedback1, feedback2));

        List<FeedbackEntity> result =
                feedbackService.getAllFeedbacks(FeedbackSortState.DATE_ASCENDING);

        assertEquals(2, result.size());
        assertEquals(feedback1, result.get(1));
        assertEquals(feedback2, result.get(0));
    }

    @Test
    void testAddFeedback() {
        FeedbackDto feedbackDto = new FeedbackDto();
        feedbackDto.setEmail("test@example.com");
        feedbackDto.setFeedback("Test feedback");

        PersonEntity person = new PersonEntity();
        person.setEmail("test@example.com");

        when(personRepository.findPersonEntityByEmail(feedbackDto.getEmail()))
                .thenReturn(Optional.of(person));

        FeedbackEntity savedFeedback = new FeedbackEntity(person, "Test feedback", 0);
        savedFeedback.setDateTime(LocalDateTime.now());
        when(feedbackRepository.save(Mockito.any(FeedbackEntity.class))).thenReturn(savedFeedback);

        FeedbackEntity result = feedbackService.addFeedback(feedbackDto);

        assertNotNull(result);
        assertEquals(feedbackDto.getFeedback(), result.getFeedback());
    }

    @Test
    void testAddFeedbackWithInvalidEmail() {
        FeedbackDto feedbackDto = new FeedbackDto();
        feedbackDto.setEmail("");

        assertThrows(NullPointerException.class, () -> feedbackService.addFeedback(feedbackDto));
    }

    @Test
    void testAddFeedbackWithNonExistingPerson() {
        FeedbackDto feedbackDto = new FeedbackDto();
        feedbackDto.setEmail("nonexisting@example.com");

        when(personRepository.findPersonEntityByEmail(feedbackDto.getEmail()))
                .thenReturn(Optional.empty());

        assertThrows(NullPointerException.class, () -> feedbackService.addFeedback(feedbackDto));
    }

    @Test
    void testIncrementLikeAndSavePerson() {
        FeedbackEntity feedback = new FeedbackEntity();
        PersonEntity person = new PersonEntity();

        when(feedbackRepository.findById(1L)).thenReturn(Optional.of(feedback));
        when(personRepository.findById(2L)).thenReturn(Optional.of(person));

        FeedbackEntity result = feedbackService.incrementLikeAndSavePerson(1L, 2L);

        assertNotNull(result);
        assertTrue(result.getLikedByPerson().contains(person));
    }

    @Test
    void testDecrementLikeAndRemovePerson() {
        FeedbackEntity feedback = new FeedbackEntity();
        PersonEntity person = new PersonEntity();

        feedback.getLikedByPerson().add(person);

        when(feedbackRepository.findById(1L)).thenReturn(Optional.of(feedback));
        when(personRepository.findById(2L)).thenReturn(Optional.of(person));

        FeedbackEntity result = feedbackService.decrementLikeAndRemovePerson(1L, 2L);

        assertNotNull(result);
        assertFalse(result.getLikedByPerson().contains(person));
    }

    @Test
    void testDeleteFeedback() {
        FeedbackEntity feedback = new FeedbackEntity();
        feedback.setId(1L);

        when(feedbackRepository.findById(1L)).thenReturn(Optional.of(feedback));

        FeedbackEntity result = feedbackService.deleteFeedback(1L);

        assertNotNull(result);
        assertNull(result.getLikedByPerson());
    }
}