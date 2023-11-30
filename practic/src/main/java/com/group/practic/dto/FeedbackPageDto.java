package com.group.practic.dto;

import com.group.practic.entity.FeedbackEntity;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
public class FeedbackPageDto {
    long totalFeedbacks;
    int totalPages;
    List<FeedbackDto> feedbacksOnPage;

    public static FeedbackPageDto map(Page<FeedbackEntity> page) {
        FeedbackPageDto dto = new FeedbackPageDto();
        dto.totalFeedbacks = page.getTotalElements();
        dto.totalPages = page.getTotalPages();
        dto.feedbacksOnPage = page.get().map(FeedbackDto::map).toList();
        return dto;
    }
}
