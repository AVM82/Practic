package com.group.practic.repository;

import com.group.practic.entity.ChapterEntity;
import com.group.practic.entity.TopicReportEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicReportRepository extends JpaRepository<TopicReportEntity, Long> {
    List<TopicReportEntity> findByChapter(ChapterEntity chapter);

}
