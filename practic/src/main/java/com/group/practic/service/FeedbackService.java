package com.group.practic.service;

import com.group.practic.dto.FeedbackDto;
import com.group.practic.entity.FeedbackEntity;
import com.group.practic.entity.PersonEntity;
import com.group.practic.enumeration.FeedbackSortState;
import com.group.practic.repository.FeedbackRepository;
import com.group.practic.repository.PersonRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeedbackService {
    Logger logger= LoggerFactory.getLogger(FeedbackService.class);
    @Autowired
    FeedbackRepository repository;

    @Autowired
    PersonRepository personRepository;

    public List<FeedbackEntity> getAllFeedbacks(FeedbackSortState sortState) {
        ArrayList<FeedbackEntity> list = new ArrayList<>(repository.findAll());
        list.sort(Comparator.comparing(FeedbackEntity::getDateTime));
        return getSortFeedbackList(list,sortState);
    }

    private List<FeedbackEntity> getSortFeedbackList(ArrayList<FeedbackEntity> list, FeedbackSortState sortState) {
    switch (sortState){
        case DATE_DESCENDING -> list.sort(Comparator.comparing(FeedbackEntity::getDateTime));
        case DATE_ASCENDING -> list.sort(Comparator.comparing(FeedbackEntity::getDateTime).reversed());
        case RATING_DESCENDING -> list.sort(Comparator.comparing(FeedbackEntity::getLikes));
        case RATING_ASCENDING -> list.sort(Comparator.comparing(FeedbackEntity::getLikes).reversed());
    }
    return list;
    }


    public FeedbackEntity addFeedback(FeedbackDto feedbackDto) {
        String email = feedbackDto.getEmail();
        logger.info(email);

        PersonEntity person = personRepository.findPersonEntityByEmail(email).orElse(null);
        logger.info(person.toString());
        if (email.isEmpty() || person == null) {
            throw new NullPointerException();
        }
        FeedbackEntity feedback = new FeedbackEntity(person, feedbackDto.getFeedback(), 0);
        feedback.setDateTime(LocalDateTime.now());
        repository.save(feedback);
        return feedback;
    }

    public FeedbackEntity incrementLikeAndSavePerson(Long idFeedback, Long idPerson) {
        Optional<FeedbackEntity> feedbackOption = repository.findById(idFeedback);
        Optional<PersonEntity> personOption = personRepository.findById(idPerson);
        if (personOption.isPresent() && feedbackOption.isPresent()) {
            FeedbackEntity feedback = feedbackOption.get();
            PersonEntity person = personOption.get();
            if (!feedback.getLikedByPerson().contains(person)) {
                feedback.getLikedByPerson().add(person);
                repository.incrementLikesById(idFeedback);
                repository.save(feedback);
                return feedback;
            }
        }
        return null;
    }

    public FeedbackEntity decrementLikeAndRemovePerson(Long idFeedback, Long idPerson) {
        Optional<FeedbackEntity> feedbackOption = repository.findById(idFeedback);
        Optional<PersonEntity> personOption = personRepository.findById(idPerson);
        if (feedbackOption.isPresent() && personOption.isPresent()) {
            FeedbackEntity feedbackEntity = feedbackOption.get();
            PersonEntity person = personOption.get();
            if (feedbackEntity.getLikedByPerson().contains(person)) {
                feedbackEntity.getLikedByPerson().remove(person);
                repository.decrementLikesById(idFeedback);
                repository.save(feedbackEntity);
                return feedbackEntity;
            }
        }
        return null;
    }
}
