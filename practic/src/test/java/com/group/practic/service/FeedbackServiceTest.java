package com.group.practic.service;

import static com.group.practic.enumeration.FeedbackSortState.DATE_ASCENDING;
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
import com.group.practic.dto.FeedbackPageDto;
import com.group.practic.entity.FeedbackEntity;
import com.group.practic.entity.PersonEntity;
import com.group.practic.entity.RoleEntity;
import com.group.practic.enumeration.FeedbackSortState;
import com.group.practic.repository.FeedbackRepository;
import java.util.ArrayList;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
    void testGetAllFeedbacksPaginated() {
        int page = 0;
        int size = 10;
        FeedbackSortState sortState = FeedbackSortState.RATING_DESCENDING;
        PersonEntity person = new PersonEntity();
        List<FeedbackEntity> feedbackEntities = new ArrayList<>();
        feedbackEntities.add(new FeedbackEntity(person, "test1"));
        feedbackEntities.add(new FeedbackEntity(person, "test2"));

        Pageable pageable = PageRequest.of(page, size, Sort.by("likes").descending());
        Page<FeedbackEntity> feedbackPage = new PageImpl<>(feedbackEntities, pageable, feedbackEntities.size());

        when(feedbackRepository.findAll(pageable)).thenReturn(feedbackPage);

        FeedbackPageDto result = feedbackService.getAllFeedbacksPaginated(page, size, sortState);

        assertEquals(feedbackEntities.size(), result.getTotalFeedbacks());
        assertEquals(1, result.getTotalPages());
        assertEquals(2, result.getTotalFeedbacks());
        assertEquals(2, result.getFeedbacksOnPage().size());

        List<FeedbackDto> feedbacks = result.getFeedbacksOnPage();
        assertEquals("test1", feedbacks.get(0).getFeedback());

        feedbacks.get(1).setLikes(1);
        assertEquals(1, feedbacks.get(1).getLikes());

        verify(feedbackRepository, times(1)).findAll(pageable);
    }

    @Test
    void testSortByDateAscending() {
        int page = 0;
        int size = 10;
        PersonEntity person = new PersonEntity();

        List<FeedbackEntity> feedbackEntities = new ArrayList<>();
        feedbackEntities.add(new FeedbackEntity(person, "test1"));
        feedbackEntities.add(new FeedbackEntity(person, "test2"));


        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<FeedbackEntity> feedbackPage = new PageImpl<>(feedbackEntities, pageable, feedbackEntities.size());

        when(feedbackRepository.findAll(pageable)).thenReturn(feedbackPage);

        FeedbackPageDto result = feedbackService.getAllFeedbacksPaginated(page, size, DATE_ASCENDING);

        assertEquals(feedbackEntities.size(), result.getTotalFeedbacks());
        assertEquals(1, result.getTotalPages());
        assertEquals(2, result.getTotalFeedbacks());
        assertEquals(2, result.getFeedbacksOnPage().size());

        List<FeedbackDto> feedbacks = result.getFeedbacksOnPage();
        assertEquals("test1", feedbacks.get(0).getFeedback());
        assertEquals("test2", feedbacks.get(1).getFeedback());

        feedbacks.get(1).setLikes(1);
        assertEquals(1, feedbacks.get(1).getLikes());

        verify(feedbackRepository, times(1)).findAll(pageable);

    }

    @Test
    void testSortByRatingDescending() {
        int page = 0;
        int size = 10;
        FeedbackSortState sortState = FeedbackSortState.RATING_DESCENDING;
        PersonEntity person = new PersonEntity();

        List<FeedbackEntity> feedbackEntities = new ArrayList<>();
        FeedbackEntity feedback = new FeedbackEntity(new PersonEntity(), "test1");
        feedback.setLikes(3);
        feedbackEntities.add(feedback);
        feedbackEntities.add(new FeedbackEntity(person, "test2"));

        Pageable pageable = PageRequest.of(page, size, Sort.by("likes").descending());
        Page<FeedbackEntity> feedbackPage = new PageImpl<>(feedbackEntities, pageable, feedbackEntities.size());

        when(feedbackRepository.findAll(pageable)).thenReturn(feedbackPage);

        FeedbackPageDto result = feedbackService.getAllFeedbacksPaginated(page, size, sortState);

        assertEquals(feedbackEntities.size(), result.getTotalFeedbacks());
        assertEquals(1, result.getTotalPages());
        assertEquals(2, result.getTotalFeedbacks());
        assertEquals(2, result.getFeedbacksOnPage().size());

        List<FeedbackDto> feedbacks = result.getFeedbacksOnPage();

        assertEquals(3, feedbacks.get(0).getLikes());

        verify(feedbackRepository, times(1)).findAll(pageable);
    }

    @Test
    void testSortByRatingAscending() {
        int page = 0;
        int size = 10;
        FeedbackSortState sortState = FeedbackSortState.RATING_ASCENDING;
        PersonEntity person = new PersonEntity();
        FeedbackEntity feedback = new FeedbackEntity(person, "test2");
        feedback.setLikes(5);

        List<FeedbackEntity> feedbackEntities = new ArrayList<>();
        feedbackEntities.add(new FeedbackEntity(person, "test1"));
        feedbackEntities.add(feedback);

        Pageable pageable = PageRequest.of(page, size, Sort.by("likes").ascending());
        Page<FeedbackEntity> feedbackPage = new PageImpl<>(feedbackEntities, pageable, feedbackEntities.size());

        when(feedbackRepository.findAll(pageable)).thenReturn(feedbackPage);

        FeedbackPageDto result = feedbackService.getAllFeedbacksPaginated(page, size, sortState);

        assertEquals(feedbackEntities.size(), result.getTotalFeedbacks());
        assertEquals(1, result.getTotalPages());
        assertEquals(2, result.getTotalFeedbacks());
        assertEquals(2, result.getFeedbacksOnPage().size());

        List<FeedbackDto> feedbacks = result.getFeedbacksOnPage();
        assertEquals("test1", feedbacks.get(0).getFeedback());
        assertEquals("test2", feedbacks.get(1).getFeedback());

        assertEquals(5, feedbacks.get(1).getLikes());

        verify(feedbackRepository, times(1)).findAll(pageable);
    }

    @Test
    void testDefaultSort() {
        int page = 0;
        int size = 10;
        PersonEntity person = new PersonEntity();
        PersonEntity personTwo = new PersonEntity();
        PersonEntity personThree = new PersonEntity();

        List<FeedbackEntity> feedbackEntities = new ArrayList<>();
        feedbackEntities.add(new FeedbackEntity(person, "test1"));
        feedbackEntities.add(new FeedbackEntity(person, "test2"));
        feedbackEntities.add(new FeedbackEntity(personTwo, "test3"));
        feedbackEntities.add(new FeedbackEntity(personThree, "test4"));
        feedbackEntities.add(new FeedbackEntity(personTwo, "test5"));

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<FeedbackEntity> feedbackPage = new PageImpl<>(feedbackEntities, pageable, feedbackEntities.size());

        when(feedbackRepository.findAll(any(Pageable.class))).thenReturn(feedbackPage);

        FeedbackPageDto result = feedbackService.getAllFeedbacksPaginated(page, size, DATE_DESCENDING);

        assertEquals(feedbackEntities.size(), result.getTotalFeedbacks());
        assertEquals(1, result.getTotalPages());
        assertEquals(5, result.getTotalFeedbacks());
        assertEquals(5, result.getFeedbacksOnPage().size());

        List<FeedbackDto> feedbacks = result.getFeedbacksOnPage();
        assertEquals("test1", feedbacks.get(0).getFeedback());
        assertEquals("test2", feedbacks.get(1).getFeedback());
        assertEquals("test3", feedbacks.get(2).getFeedback());
        assertEquals("test4", feedbacks.get(3).getFeedback());
        assertEquals("test5", feedbacks.get(4).getFeedback());

        feedbacks.get(1).setLikes(1);
        assertEquals(1, feedbacks.get(1).getLikes());

        verify(feedbackRepository, times(1)).findAll(pageable);
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