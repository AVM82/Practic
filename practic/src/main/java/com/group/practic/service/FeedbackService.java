package com.group.practic.service;

import com.group.practic.dto.FeedbackDto;
import com.group.practic.dto.FeedbackPageDto;
import com.group.practic.entity.FeedbackEntity;
import com.group.practic.enumeration.FeedbackSortState;
import com.group.practic.repository.FeedbackRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class FeedbackService {

    FeedbackRepository feedbackRepository;

    PersonService personService;


    @Autowired
    public FeedbackService(FeedbackRepository feedbackRepository, PersonService personService) {
        this.feedbackRepository = feedbackRepository;
        this.personService = personService;
    }

    public Optional<FeedbackEntity> get(Long id) {
        return feedbackRepository.findById(id);
    }


    public FeedbackPageDto getAllFeedbacksPaginated(
            int page, int size, FeedbackSortState sortState) {
        return FeedbackPageDto.map(feedbackRepository.findAll(getPageable(page, size, sortState)));
    }


    protected Pageable getPageable(int page, int size, FeedbackSortState sortState) {
        Sort sort = switch (sortState) {
            case DATE_ASCENDING -> Sort.by("id").ascending();
            case RATING_DESCENDING -> Sort.by("likes").descending();
            case RATING_ASCENDING -> Sort.by("likes").ascending();
            default -> Sort.by("id").descending();
        };
        return PageRequest.of(page, size, sort);
    }

    public FeedbackDto addFeedback(String feedback) {
        return FeedbackDto.map(feedbackRepository
                .save(new FeedbackEntity(PersonService.me(), feedback)));
    }


    public FeedbackEntity incrementLike(FeedbackEntity feedback) {
        Long personId = PersonService.me().getId();
        if (feedback.getLikedByPerson().add(personId)) {
            feedback.setLikes(feedback.getLikes() + 1);
            return feedbackRepository.save(feedback);
        }
        return feedback;
    }


    public FeedbackEntity decrementLike(FeedbackEntity feedback) {
        Long personId = PersonService.me().getId();
        if (feedback.getLikedByPerson().remove(personId)) {
            feedback.setLikes(feedback.getLikes() - 1);
            return feedbackRepository.save(feedback);
        }
        return feedback;
    }


    public void deleteFeedback(Long id) {
        if (id > 0) {
            feedbackRepository.deleteById(id);
        }
    }
}
