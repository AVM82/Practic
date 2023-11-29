package com.group.practic.controller;

import static com.group.practic.util.ResponseUtils.badRequest;
import static com.group.practic.util.ResponseUtils.getResponse;

import com.group.practic.dto.FeedbackDto;
import com.group.practic.dto.FeedbackLikesDto;
import com.group.practic.dto.FeedbackPage;
import com.group.practic.entity.FeedbackEntity;
import com.group.practic.enumeration.FeedbackSortState;
import com.group.practic.service.FeedbackService;
import java.util.Optional;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/feedbacks")
public class FeedbackController {

    private final FeedbackService service;

    @Autowired
    public FeedbackController(FeedbackService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<FeedbackPage> getAllFeedbacksPaginated(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam FeedbackSortState sortState) {
        return ResponseEntity.ok(service.getAllFeedbacksPaginated(page, size, sortState));
    }


    @PostMapping("/")
    @PreAuthorize("!hasRole('GUEST')")
    public ResponseEntity<FeedbackDto> addFeedback(@RequestBody String feedback) {
        return feedback == null || feedback.trim().length() < 5 ? badRequest()
                : getResponse(service.addFeedback(feedback));
    }

    @PatchMapping("/add/{id}")
    public ResponseEntity<FeedbackLikesDto> incrementLike(
            @PathVariable Long id,
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam FeedbackSortState sortState) {
        Optional<FeedbackEntity> feedbackEntity = service.get(id);
        return getResponse(feedbackEntity
                .map(entity -> new FeedbackLikesDto(service.incrementLike(entity)))
                .orElseGet(() -> new FeedbackLikesDto(
                        service.getAllFeedbacksPaginated(page, size, sortState))));
    }


    @PatchMapping("/remove/{id}")
    public ResponseEntity<FeedbackLikesDto> decrementLike(
            @PathVariable Long id,
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam FeedbackSortState sortState) {
        Optional<FeedbackEntity> feedbackEntity = service.get(id);
        return getResponse(feedbackEntity
                .map(entity -> new FeedbackLikesDto(service.decrementLike(entity)))
                .orElseGet(() -> new FeedbackLikesDto(
                        service.getAllFeedbacksPaginated(page, size, sortState))));

    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<FeedbackPage> deleteFeedback(
            @PathVariable Long id,
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam FeedbackSortState sortState) {
        service.deleteFeedback(id);
        return ResponseEntity.ok(service.getAllFeedbacksPaginated(page, size, sortState));
    }
}
