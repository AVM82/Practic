package com.group.practic.dto;

import com.group.practic.entity.FeedbackEntity;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
public class FeedbackPage {
    long totalFeedbacks;
    int totalPages;
    List<FeedbackDto> feedbacksOnPage;

    public static FeedbackPage map(Page<FeedbackEntity> page) {
        FeedbackPage dto = new FeedbackPage();
        dto.totalFeedbacks = page.getTotalElements();
        dto.totalPages = page.getTotalPages();
        dto.feedbacksOnPage = page.get().map(FeedbackDto::map).toList();
        return dto;
    }
}
