package com.group.practic.service;

import com.group.practic.dto.FeedbackDto;
import com.group.practic.entity.FeedbackEntity;
import com.group.practic.entity.PersonEntity;
import com.group.practic.repository.FeedbackRepository;
import com.group.practic.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FeedbackService {
    Logger logger = LoggerFactory.getLogger(FeedbackService.class);
    @Autowired
    FeedbackRepository repository;

    @Autowired
    PersonRepository personRepository;


    public List<FeedbackEntity> getAllFeedbacks() {
        return new ArrayList<>(repository.findAll());
    }

    public FeedbackEntity addFeedback(FeedbackDto feedbackDto) {
        String email = feedbackDto.getEmail();
        PersonEntity person = personRepository.findByEmail(email).orElse(null);

        if (email.isEmpty() || person == null) {
            throw new NullPointerException();
        }
        FeedbackEntity feedback = new FeedbackEntity(person, feedbackDto.getFeedback(), 0);
        repository.save(feedback);

        return feedback;

    }

    public FeedbackEntity incrementLikeAndSavePerson(Long idFeedback, Long idPerson) {
        Optional<FeedbackEntity> feedbackOption = repository.findById(idFeedback);
        Optional<PersonEntity> personOption = personRepository.findById(idPerson);

        if (personOption.isPresent() && feedbackOption.isPresent()) {
            FeedbackEntity feedback = feedbackOption.get();
            PersonEntity person = personOption.get();

            feedback.getLikedByPerson().add(person);
            repository.incrementLikesById(idFeedback);
            repository.save(feedback);
            return feedback;
        }
        return null;
    }

    public FeedbackEntity decrementLikeAndRemovePerson(Long idFeedback, Long idPerson) {
        logger.info("id feedback = {}", idFeedback);
        logger.info("id user = {}", idPerson);

        Optional<FeedbackEntity> feedbackOption = repository.findById(idFeedback);
        Optional<PersonEntity> personOption = personRepository.findById(idPerson);
        logger.info(personOption.toString());
        logger.info(feedbackOption.toString());

        if (feedbackOption.isPresent() && personOption.isPresent()) {
            FeedbackEntity feedbackEntity = feedbackOption.get();
            PersonEntity person = personOption.get();

            feedbackEntity.getLikedByPerson().remove(person);
            repository.decrementLikesById(idFeedback);

            repository.save(feedbackEntity);
            return feedbackEntity;
        }
        return null;
    }
}
