package com.group.practic.service;

import com.group.practic.dto.FeedbackDto;
import com.group.practic.entity.FeedbackEntity;
import com.group.practic.repository.FeedbackRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
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

    public List<FeedbackDto> getAllFeedbacks() {
        return feedbackRepository.findAll().stream().map(FeedbackDto::map).toList();
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


    public FeedbackEntity deleteFeedback(FeedbackEntity feedback) {
        feedback.setLikedByPerson(null);
        feedbackRepository.delete(feedback);
        return feedback;
    }
}
