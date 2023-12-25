package com.group.practic.service;

import com.group.practic.PropertyLoader;
import com.group.practic.entity.ChapterEntity;
import com.group.practic.entity.TopicReportEntity;
import com.group.practic.repository.TopicReportRepository;
import com.group.practic.util.PropertyUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TopicReportService {

    TopicReportRepository topicReportRepository;


    @Autowired
    public TopicReportService(TopicReportRepository topicReportRepository) {
        this.topicReportRepository = topicReportRepository;
    }


    public Collection<TopicReportEntity> getAllTopicReport() {
        List<TopicReportEntity> list = topicReportRepository.findAll();
        list.sort(Comparator.comparing(TopicReportEntity::getId));
        return list;
    }


    public TopicReportEntity addTopicReport(ChapterEntity chapter, String topic) {
        return create(chapter, topic);
    }


    public Collection<TopicReportEntity> getTopicsByChapter(ChapterEntity chapter) {
        return topicReportRepository.findByChapter(chapter);

    }


    public Optional<TopicReportEntity> get(ChapterEntity chapter, String topic) {
        return topicReportRepository.findByChapterAndTopic(chapter, topic);
    }


    public Optional<TopicReportEntity> get(long id) {
        return topicReportRepository.findById(id);
    }


    public TopicReportEntity create(ChapterEntity chapter, String topic) {
        Optional<TopicReportEntity> exist = get(chapter, topic);
        return exist
                .orElseGet(() -> topicReportRepository.save(new TopicReportEntity(chapter, topic)));
    }


    public List<TopicReportEntity> getChapterTopics(ChapterEntity chapter, PropertyLoader prop) {
        List<TopicReportEntity> result = new ArrayList<>();
        String keyStarts =
                PropertyUtil.createKeyStarts(chapter.getNumber(), PropertyUtil.TOPIC_REPORT_PART);
        for (Map.Entry<Object, Object> entry : prop.getEntrySet()) {
            String key = (String) entry.getKey();
            if (key.startsWith(keyStarts) && PropertyUtil.countDots(key) == 3
                    && PropertyUtil.getChapterNumber(3, key) != 0) {
                String topic = ((String) entry.getValue());
                TopicReportEntity topicReportEntity = create(chapter, topic);
                result.add(topicReportEntity);
            }
        }
        return result;
    }

}
