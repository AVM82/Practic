package com.group.practic.controller;

import static com.group.practic.util.ResponseUtils.getResponse;

import com.group.practic.dto.TopicReportDto;
import com.group.practic.entity.TopicReportEntity;
import com.group.practic.service.TopicReportService;
import jakarta.validation.Valid;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/topicsreports")
public class TopicReportController {

    @Autowired
    TopicReportService reportService;

    @GetMapping("/")
    public ResponseEntity<Collection<TopicReportEntity>> getAllTopics() {
        return getResponse(reportService.getAllTopicReport());
    }

    @GetMapping("/{idChapter}")
    public ResponseEntity<Collection<TopicReportEntity>>
            getTopicsByChapter(@PathVariable Long idChapter) {
        return getResponse(reportService.getTopicsByChapter(idChapter));
    }

    @PostMapping("/")
    public ResponseEntity<TopicReportEntity>
            addFeedback(@Valid @RequestBody TopicReportDto topicReport) {
        TopicReportEntity entity = reportService.addTopicReport(topicReport);
        return entity == null
                ? ResponseEntity.notFound().build() : ResponseEntity.ok(entity);
    }

}
