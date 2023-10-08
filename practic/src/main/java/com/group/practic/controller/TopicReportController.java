package com.group.practic.controller;


import com.group.practic.dto.TopicReportDTO;
import com.group.practic.entity.TopicReportEntity;
import com.group.practic.service.TopicReportService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

import static com.group.practic.util.ResponseUtils.getResponse;

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
    public ResponseEntity<Collection<TopicReportEntity>> getTopicsByChapter(@PathVariable Long idChapter) {
        return getResponse(reportService.getTopicsByChapter(idChapter));
    }

    @PostMapping("/")
    public ResponseEntity<TopicReportEntity>
    addFeedback(@Valid @RequestBody TopicReportDTO topicReportDTO) {
        TopicReportEntity entity = reportService.addTopicReport(topicReportDTO);
        return entity == null
                ? ResponseEntity.notFound().build() : ResponseEntity.ok(entity);
    }

}
