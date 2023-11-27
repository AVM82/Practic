package com.group.practic.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.group.practic.entity.ChapterEntity;
import com.group.practic.entity.TopicReportEntity;
import com.group.practic.repository.TopicReportRepository;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@Slf4j
class TopicReportServiceTest {

    @InjectMocks
    private TopicReportService topicReportService;

    @Mock
    private TopicReportRepository reportRepository;
    private TopicReportEntity topicReport1;
    private TopicReportEntity topicReport2;



    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        topicReport1 = new TopicReportEntity();
        topicReport1.setId(1L);
        topicReport1.setChapter(new ChapterEntity());
        topicReport1.setTopic("Topic 1");

        topicReport2 = new TopicReportEntity();
        topicReport2.setId(2L);
        topicReport2.setChapter(new ChapterEntity());
        topicReport2.setTopic("Topic 2");
    }

    @Test
    void testGetAllTopicReport() {
        List<TopicReportEntity> expectedReports = Arrays.asList(topicReport1, topicReport2);
        when(reportRepository.findAll()).thenReturn(expectedReports);
        Collection<TopicReportEntity> actualReports = topicReportService.getAllTopicReport();
        assertEquals(expectedReports, actualReports);
        assertEquals("2", String.valueOf(actualReports.size()));

    }

    @Test
    void testGetTopicsByChapter() {
        ChapterEntity chapter = new ChapterEntity();
        Collection<TopicReportEntity> result = topicReportService.getTopicsByChapter(chapter);
        assertNotNull(result);
        verify(reportRepository, times(1)).findByChapter(chapter);
    }

    @Test
    void testGet() {
        ChapterEntity chapter = new ChapterEntity();
        String topic = "Test Topic";
        when(reportRepository.findByChapterAndTopic(chapter, topic))
                .thenReturn(Optional.of(topicReport1));
        Optional<TopicReportEntity> result = topicReportService.get(chapter, topic);
        assertTrue(result.isPresent());
        assertEquals(topicReport1, result.get());
        verify(reportRepository, times(1))
                .findByChapterAndTopic(chapter, topic);
    }

    @Test
    void testCreate() {
        ChapterEntity chapter = new ChapterEntity();
        String topic = "Test Topic";
        when(reportRepository.save(any(TopicReportEntity.class))).thenReturn(topicReport1);
        TopicReportEntity result = topicReportService.create(chapter, topic);
        assertNotNull(result);
        assertEquals(topicReport1, result);
        verify(reportRepository, times(1)).save(any(TopicReportEntity.class));
    }

    @Test
     void testAddTopicReport() {
        ChapterEntity chapter = new ChapterEntity();
        String topic = "Test Topic";
        TopicReportEntity topicReportEntity = new TopicReportEntity(chapter, topic);
        when(reportRepository.findByChapterAndTopic(chapter, topic))
                .thenReturn(Optional.empty());
        when(reportRepository.save(any(TopicReportEntity.class)))
                .thenReturn(topicReportEntity);
        TopicReportEntity result = topicReportService.addTopicReport(chapter, topic);
        assertNotNull(result);
        assertEquals(chapter, result.getChapter());
        assertEquals(topic, result.getTopic());
        verify(reportRepository, times(1))
                .findByChapterAndTopic(chapter, topic);
        verify(reportRepository, times(1))
                .save(any(TopicReportEntity.class));
    }

}