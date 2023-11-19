package com.group.practic.controller;

import static com.group.practic.util.ResponseUtils.getResponse;

import com.group.practic.dto.FeedbackDto;
import com.group.practic.service.FeedbackService;
import com.group.practic.util.ResponseUtils;
import jakarta.validation.Valid;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
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


    private final FeedbackService service;

    @Autowired
    public FeedbackController(FeedbackService service) {
        this.service = service;
    }

    @GetMapping("/")
    public ResponseEntity<Collection<FeedbackDto>> getAllFeedbacks() {
        return getResponse(service.getAllFeedbacks());
    }


    @PostMapping("/")
    @PreAuthorize("!hasRole('GUEST')")
    public ResponseEntity<FeedbackDto> addFeedback(@Valid @RequestBody String feedback) {
        return getResponse(service.addFeedback(feedback));
    }


    @PatchMapping("/add/{id}")
    public ResponseEntity<FeedbackDto> incrementLike(@PathVariable Long id) {
        return ResponseUtils.getResponse(service.get(id)
                .map(service::incrementLike)
                .map(FeedbackDto::map));
    }


    @PatchMapping("/remove/{id}")
    public ResponseEntity<FeedbackDto> decrementLike(@PathVariable Long id) {
        return ResponseUtils.getResponse(service.get(id)
                .map(service::decrementLike)
                .map(FeedbackDto::map));
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<FeedbackDto> deleteFeedback(@PathVariable Long id) {
        return ResponseUtils.getResponse(service.get(id)
                .map(service::deleteFeedback)
                .map(FeedbackDto::map));
    }

}
