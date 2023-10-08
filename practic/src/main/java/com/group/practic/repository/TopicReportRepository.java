package com.group.practic.repository;

import com.group.practic.entity.ChapterEntity;
import com.group.practic.entity.FeedbackEntity;
import com.group.practic.entity.TopicReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TopicReportRepository extends JpaRepository<TopicReportEntity, Long> {
    List<TopicReportEntity> findByChapter(ChapterEntity chapter);

}
