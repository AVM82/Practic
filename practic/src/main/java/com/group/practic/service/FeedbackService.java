package com.group.practic.service;

import com.group.practic.dto.FeedbackDto;
import com.group.practic.entity.FeedbackEntity;
import com.group.practic.enumeration.FeedbackSortState;
import com.group.practic.repository.FeedbackRepository;
import java.util.ArrayList;
import java.util.Comparator;
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

    public List<FeedbackDto> getAllFeedbacks(FeedbackSortState sortState) {
        ArrayList<FeedbackEntity> list = new ArrayList<>(feedbackRepository.findAll());
        list.sort(Comparator.comparing(FeedbackEntity::getDateTime));
        return getSortFeedbackList(list, sortState).stream().map(FeedbackDto::map).toList();
    }



    private List<FeedbackEntity> getSortFeedbackList(ArrayList<FeedbackEntity> list,
                                                     FeedbackSortState sortState) {
        switch (sortState) {
            case DATE_ASCENDING -> list
                    .sort(Comparator.comparing(FeedbackEntity::getDateTime).reversed());
            case RATING_DESCENDING -> list.sort(Comparator.comparing(FeedbackEntity::getLikes));
            case RATING_ASCENDING -> list
                    .sort(Comparator.comparing(FeedbackEntity::getLikes).reversed());
            default -> list.sort(Comparator.comparing(FeedbackEntity::getDateTime));
        }
        return list;
    }


    public FeedbackDto addFeedback(String feedback) {
        return FeedbackDto.map(feedbackRepository.save(new FeedbackEntity(PersonService.me(), feedback)));
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
