package com.group.practic.service;

import com.group.practic.dto.FeedbackDto;
import com.group.practic.entity.FeedbackEntity;
import com.group.practic.entity.PersonEntity;
import com.group.practic.repository.FeedbackRepository;
import com.group.practic.repository.PersonRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeedbackService {
    @Autowired
    FeedbackRepository repository;

    @Autowired
    PersonRepository personRepository;

    public List<FeedbackEntity> getAllFeedbacks() {
        ArrayList<FeedbackEntity> list = new ArrayList<>(repository.findAll());
        list.sort(Comparator.comparing(FeedbackEntity::getDateTime));
        return list;
    }

    public FeedbackEntity addFeedback(FeedbackDto feedbackDto) {
        String email = feedbackDto.getEmail();
        PersonEntity person = personRepository.findPersonEntityByEmail(email).orElse(null);
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
