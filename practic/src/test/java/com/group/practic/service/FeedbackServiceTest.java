package com.group.practic.service;

import static com.group.practic.enumeration.FeedbackSortState.DATE_DESCENDING;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.group.practic.dto.FeedbackDto;
import com.group.practic.entity.FeedbackEntity;
import com.group.practic.entity.PersonEntity;
import com.group.practic.entity.RoleEntity;
import com.group.practic.enumeration.FeedbackSortState;
import com.group.practic.repository.FeedbackRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

class FeedbackServiceTest {

    @InjectMocks
    private FeedbackService feedbackService;

    @Mock
    private FeedbackRepository feedbackRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testAddFeedback() {
        String feedbackText = "Test feedback";
        String wrongText = "Test feedback2";

        PersonEntity person = new PersonEntity();
        person.setId(1L);
        person.setName("John");
        person.setEmail("test@example.com");
        person.setRoles(Set.of(new RoleEntity("GUEST")));

        try (MockedStatic<PersonService> utilities = mockStatic(PersonService.class)) {
            utilities.when(PersonService::me).thenReturn(person);

            FeedbackEntity savedFeedbackEntity = new FeedbackEntity(person, feedbackText);
            when(feedbackRepository.save(any(FeedbackEntity.class))).thenReturn(savedFeedbackEntity);

            List<FeedbackDto> getAllFeedbacksList = List.of(feedbackService.addFeedback(feedbackText));

            FeedbackDto result = feedbackService.addFeedback(feedbackText);

            assertNotNull(result);
            assertNotEquals(wrongText, result.getFeedback());
            assertEquals(feedbackText, result.getFeedback());
            assertEquals(1, getAllFeedbacksList.size());

            verify(feedbackRepository, times(2)).save(any(FeedbackEntity.class));
        }
    }

    @Test
    void testGetPageable() {
        int page = 0;
        int size = 10;

        Pageable result = feedbackService.getPageable(page, size, FeedbackSortState.RATING_ASCENDING);
        assertEquals(page, result.getPageNumber());
        assertEquals(size, result.getPageSize());
        assertEquals(Sort.by(Sort.Direction.ASC, "likes"), result.getSort());
        assertNotEquals(Sort.by(Sort.Direction.ASC, "id"), result.getSort());
        assertNotEquals(Sort.by(Sort.Direction.DESC, "id"), result.getSort());

        result = feedbackService.getPageable(page, size, FeedbackSortState.RATING_DESCENDING);
        assertEquals(page, result.getPageNumber());
        assertEquals(size, result.getPageSize());
        assertEquals(Sort.by(Sort.Direction.DESC, "likes"), result.getSort());
        assertNotEquals(Sort.by(Sort.Direction.ASC, "id"), result.getSort());
        assertNotEquals(Sort.by(Sort.Direction.DESC, "id"), result.getSort());

        result = feedbackService.getPageable(page, size, FeedbackSortState.DATE_ASCENDING);
        assertEquals(page, result.getPageNumber());
        assertEquals(size, result.getPageSize());
        assertEquals(Sort.by(Sort.Direction.ASC, "id"), result.getSort());
        assertNotEquals(Sort.by(Sort.Direction.ASC, "likes"), result.getSort());
        assertNotEquals(Sort.by(Sort.Direction.DESC, "likes"), result.getSort());

        result = feedbackService.getPageable(page, size, DATE_DESCENDING);
        assertEquals(page, result.getPageNumber());
        assertEquals(size, result.getPageSize());
        assertEquals(Sort.by(Sort.Direction.DESC, "id"), result.getSort());
        assertNotEquals(Sort.by(Sort.Direction.ASC, "likes"), result.getSort());
        assertNotEquals(Sort.by(Sort.Direction.DESC, "likes"), result.getSort());

    }
    @Test
    void testGetFeedbackById() {
        long feedbackId = 1L;
        FeedbackEntity feedbackEntity = new FeedbackEntity();
        feedbackEntity.setId(feedbackId);

        when(feedbackRepository.findById(feedbackId)).thenReturn(Optional.of(feedbackEntity));
        Optional<FeedbackEntity> result = feedbackService.get(feedbackId);

        assertTrue(result.isPresent());
        assertEquals(feedbackId, result.get().getId());

        verify(feedbackRepository, times(1)).findById(feedbackId);
    }

    @Test
    void testGetFeedbackByIdNotFound() {
        long nonExistentFeedbackId = 10L;

        when(feedbackRepository.findById(nonExistentFeedbackId)).thenReturn(Optional.empty());

        Optional<FeedbackEntity> result = feedbackService.get(nonExistentFeedbackId);

        assertFalse(result.isPresent());

        verify(feedbackRepository, times(1)).findById(nonExistentFeedbackId);
    }

    @Test
    void testGetFeedback() {
        String feedbackText = "Test feedback";

        PersonEntity person = new PersonEntity();
        person.setId(1L);
        person.setName("John");
        person.setEmail("test@example.com");
        person.setRoles(Set.of(new RoleEntity("GUEST")));

        try (MockedStatic<PersonService> utilities = mockStatic(PersonService.class)) {
            utilities.when(PersonService::me).thenReturn(person);

            FeedbackEntity savedFeedbackEntity = new FeedbackEntity(person, feedbackText);
            when(feedbackRepository.save(any(FeedbackEntity.class))).thenReturn(savedFeedbackEntity);
            when(feedbackRepository.findById(person.getId())).thenReturn(Optional.of(savedFeedbackEntity));

            Optional<FeedbackEntity> feedbacks = feedbackService.get(1L);
            assertTrue(feedbacks.isPresent());

            String result = feedbacks.get().getFeedback();

            assertEquals(feedbackText, result);

            verify(feedbackRepository, times(1)).findById(any(Long.class));
        }
    }

    @Test
    void testDeleteFeedbackValidId() {
        long feedbackId = 1L;

        feedbackService.deleteFeedback(feedbackId);

        verify(feedbackRepository, times(1)).deleteById(feedbackId);
    }

    @Test
    void testDeleteFeedbackInvalidId() {
        long invalidFeedbackId = -1L;

        feedbackService.deleteFeedback(invalidFeedbackId);

        verify(feedbackRepository, never()).deleteById(anyLong());
    }

    @Test
    void testIncrementLikeValidFeedbackEntityReturnsIncrementedLikes() {
        FeedbackEntity feedbackEntity = new FeedbackEntity();
        feedbackEntity.setId(1L);
        feedbackEntity.setLikes(0);
        PersonEntity person = new PersonEntity();

        try (MockedStatic<PersonService> utilities = mockStatic(PersonService.class)) {
            utilities.when(PersonService::me).thenReturn(person);

            when(feedbackRepository.findById(anyLong())).thenReturn(Optional.of(feedbackEntity));
            when(feedbackRepository.save(any(FeedbackEntity.class))).thenReturn(feedbackEntity);

            FeedbackEntity result = feedbackService.incrementLike(feedbackEntity);

            assertNotNull(result);
            assertEquals(1, result.getLikes());
        }
    }

    @Test
    void testIncrementLike() {
        long personId = 1L;
        long feedbackId = 10L;

        PersonEntity person = new PersonEntity();
        person.setId(personId);

        try (MockedStatic<PersonService> utilities = mockStatic(PersonService.class)) {
            utilities.when(PersonService::me).thenReturn(person);

            FeedbackEntity feedback = new FeedbackEntity();
            feedback.setId(feedbackId);
            feedback.setLikes(5);

            Set<Long> likedBy = new HashSet<>();
            feedback.setLikedByPerson(likedBy);

            when(feedbackRepository.findById(feedbackId)).thenReturn(Optional.of(feedback));
            when(feedbackRepository.save(feedback)).thenReturn(feedback);

            FeedbackEntity result = feedbackService.incrementLike(feedback);

            assertEquals(6, result.getLikes());
            assertEquals(1, result.getLikedByPerson().size());

            FeedbackEntity secondResult = feedbackService.incrementLike(result);
            assertEquals(6, secondResult.getLikes());
            assertEquals(1, secondResult.getLikedByPerson().size());
        }
    }

    @Test
    void testDecrementLike() {
        long personId = 1L;
        long feedbackId = 10L;

        PersonEntity person = new PersonEntity();
        person.setId(personId);

        try (MockedStatic<PersonService> utilities = mockStatic(PersonService.class)) {
            utilities.when(PersonService::me).thenReturn(person);

            FeedbackEntity feedback = new FeedbackEntity();
            feedback.setId(feedbackId);
            feedback.setLikes(5);

            Set<Long> likedBy = new HashSet<>();
            likedBy.add(personId);
            feedback.setLikedByPerson(likedBy);

            when(feedbackRepository.findById(feedbackId)).thenReturn(Optional.of(feedback));
            when(feedbackRepository.save(feedback)).thenReturn(feedback);

            assertEquals(1, feedback.getLikedByPerson().size());

            FeedbackEntity result = feedbackService.decrementLike(feedback);

            assertEquals(4, result.getLikes());
            assertEquals(0, result.getLikedByPerson().size());

            FeedbackEntity secondResult = feedbackService.decrementLike(result);
            assertEquals(4, secondResult.getLikes());
            assertEquals(0, secondResult.getLikedByPerson().size());
        }
    }

}