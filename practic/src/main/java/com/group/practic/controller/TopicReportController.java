package com.group.practic.controller;

import static com.group.practic.util.ResponseUtils.badRequest;
import static com.group.practic.util.ResponseUtils.getResponse;

import com.group.practic.entity.ChapterEntity;
import com.group.practic.entity.TopicReportEntity;
import com.group.practic.service.ChapterService;
import com.group.practic.service.TopicReportService;
import jakarta.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.Optional;
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

    TopicReportService reportService;

    ChapterService chapterService;


    @Autowired
    public TopicReportController(ChapterService chapterService,
            TopicReportService reportService) {
        this.chapterService = chapterService;
        this.reportService = reportService;
    }

    @GetMapping("/")
    public ResponseEntity<Collection<TopicReportEntity>> getAllTopics() {
        return getResponse(reportService.getAllTopicReport());
    }

    @GetMapping("/{idChapter}")
    public ResponseEntity<Collection<TopicReportEntity>
            > getTopicsByChapter(@PathVariable Long idChapter) {

        Optional<ChapterEntity> chapter = chapterService.get(idChapter);
        return chapter.isEmpty()
                ? badRequest()
                : getResponse(reportService.getTopicsByChapter(chapter.get()));
    }

    @PostMapping("/{idChapter}")
    public ResponseEntity<TopicReportEntity> addTopic(@PathVariable Long idChapter,
                                                      @NotBlank @RequestBody String topic) {
        Optional<ChapterEntity> chapter = chapterService.get(idChapter);
        return chapter.isEmpty()
                ? badRequest()
                : getResponse(reportService.addTopicReport(chapter.get(), topic));

    }

}
