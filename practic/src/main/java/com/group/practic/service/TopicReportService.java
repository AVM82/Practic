package com.group.practic.service;

import com.group.practic.dto.TopicReportDto;
import com.group.practic.entity.ChapterEntity;
import com.group.practic.entity.TopicReportEntity;
import com.group.practic.repository.TopicReportRepository;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TopicReportService {
    @Autowired
    TopicReportRepository reportRepository;

    @Autowired
    ChapterService chapterService;

    public Collection<TopicReportEntity> getAllTopicReport() {
        List<TopicReportEntity> list = reportRepository.findAll();
        list.sort(Comparator.comparing(TopicReportEntity::getId));
        return list;
    }

    public TopicReportEntity addTopicReport(TopicReportDto topicReportDto) {
        ChapterEntity chapterEntity =
                chapterService.get(topicReportDto.getChapterId()).orElse(null);
        if (chapterEntity != null) {
            TopicReportEntity entity =
                    new TopicReportEntity(chapterEntity, topicReportDto.getTopicReport());
            reportRepository.save(entity);
            return entity;
        }
        return null;
    }

    public Collection<TopicReportEntity> getTopicsByChapter(Long idChapter) {
        ChapterEntity chapterEntity = chapterService.get(idChapter).orElse(null);
        if (chapterEntity != null) {
            return reportRepository.findByChapter(chapterEntity);
        }
        return Collections.emptyList();
    }
}

