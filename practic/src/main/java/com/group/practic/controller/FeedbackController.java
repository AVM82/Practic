package com.group.practic.controller;

import com.group.practic.dto.FeedbackDto;
import com.group.practic.dto.FeedbackLikedDto;
import com.group.practic.entity.FeedbackEntity;
import com.group.practic.service.FeedbackService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import java.util.Collection;
import org.springframework.web.bind.annotation.*;
import static com.group.practic.util.ResponseUtils.getResponse;

@RestController
@RequestMapping("/api/feedbacks")
public class FeedbackController {
    Logger logger = LoggerFactory.getLogger(FeedbackController.class);
    @Autowired
    FeedbackService service;

    @GetMapping("/")
    public ResponseEntity<Collection<FeedbackEntity>> getAllFeedbacks() {
        return getResponse(service.getAllFeedbacks());
    }

    @PostMapping("/")
    public ResponseEntity<FeedbackEntity> addFeedback(@Valid @RequestBody FeedbackDto feedbackDto) {
        FeedbackEntity entity = service.addFeedback(feedbackDto);
        return ResponseEntity.ok(entity);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<FeedbackEntity> incrementLike(@PathVariable Long id, @RequestBody Long idPerson) {
        FeedbackEntity feedback = service.incrementLikeAndSavePerson(id, idPerson);
        return feedback == null ?
                ResponseEntity.notFound().build() : ResponseEntity.ok(feedback);
    }

    @PatchMapping("/")
    public ResponseEntity<FeedbackEntity> decrementLike(@Valid @RequestBody FeedbackLikedDto dto) {
        FeedbackEntity feedback = service.decrementLikeAndRemovePerson(dto.getFeedbackId(), dto.getPersonId());
        return feedback == null ?
                ResponseEntity.notFound().build() : ResponseEntity.ok(feedback);
    }

}
