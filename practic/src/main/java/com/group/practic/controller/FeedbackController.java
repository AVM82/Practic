package com.group.practic.controller;

import static com.group.practic.util.ResponseUtils.getResponse;

import com.group.practic.dto.FeedbackDto;
import com.group.practic.dto.FeedbackLikedDto;
import com.group.practic.entity.FeedbackEntity;
import com.group.practic.service.FeedbackService;
import jakarta.validation.Valid;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/feedbacks")
public class FeedbackController {
    @Autowired
    FeedbackService service;

    @GetMapping("/")
    public ResponseEntity<Collection<FeedbackEntity>> getAllFeedbacks() {
        return getResponse(service.getAllFeedbacks());
    }

    @PostMapping("/")
    public ResponseEntity<FeedbackEntity>
            addFeedback(@Valid @RequestBody FeedbackDto feedbackDto) {
        FeedbackEntity entity = service.addFeedback(feedbackDto);
        return ResponseEntity.ok(entity);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<FeedbackEntity>
            incrementLike(@PathVariable Long id, @RequestBody Long idPerson) {
        FeedbackEntity feedback = service.incrementLikeAndSavePerson(id, idPerson);
        return feedback == null
                ? ResponseEntity.notFound().build() : ResponseEntity.ok(feedback);
    }

    @PatchMapping("/")
    public ResponseEntity<FeedbackEntity> decrementLike(@Valid @RequestBody FeedbackLikedDto dto) {
        FeedbackEntity feedback =
                service.decrementLikeAndRemovePerson(dto.getFeedbackId(), dto.getPersonId());
        return feedback == null
                ? ResponseEntity.notFound().build() : ResponseEntity.ok(feedback);
    }

}