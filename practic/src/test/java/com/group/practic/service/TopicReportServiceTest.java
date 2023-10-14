package com.group.practic.service;

import com.group.practic.dto.TopicReportDto;
import com.group.practic.entity.ChapterEntity;
import com.group.practic.entity.TopicReportEntity;
import com.group.practic.repository.TopicReportRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Slf4j

public class TopicReportServiceTest {

    @InjectMocks
    private TopicReportService topicReportService;

    @Mock
    private ChapterService chapterService;
    @Mock
    private TopicReportRepository reportRepository;
    private TopicReportEntity topicReport1;
    private TopicReportEntity topicReport2;
    private TopicReportDto topicReportDto;

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
    void testAddTopicReport() {
        topicReportDto = new TopicReportDto();
        topicReportDto.setChapterId(1L);
        topicReportDto.setTopicReport("Test report");
        ChapterEntity chapterEntity = new ChapterEntity();
        when(chapterService.get(1L)).thenReturn(Optional.of(chapterEntity));

        TopicReportEntity savedEntity = new TopicReportEntity();
        when(reportRepository.save(any(TopicReportEntity.class))).thenReturn(savedEntity);

        TopicReportEntity result = topicReportService.addTopicReport(topicReportDto);

        verify(chapterService).get(1L);

        verify(reportRepository).save(any(TopicReportEntity.class));

        assertNotNull(result);
        assertEquals(result.getChapter(), chapterEntity);
        assertEquals(result.getTopic(), topicReportDto.getTopicReport());
    }


    @Test
    void testAddTopicReportWithInvalidChapter() {
        long chapterId = 1L;
        topicReportDto = new TopicReportDto();
        topicReportDto.setChapterId(chapterId);

        when(chapterService.get(chapterId)).thenReturn(Optional.empty());

        TopicReportEntity actualReport = topicReportService.addTopicReport(topicReportDto);

        assertNull(actualReport);
        verify(chapterService, times(1)).get(chapterId);
        verify(reportRepository, never()).save(any());
    }

    @Test
    void testGetTopicsByChapter() {
        ChapterEntity chapterEntity = new ChapterEntity();
        when(chapterService.get(1L)).thenReturn(Optional.of(chapterEntity));

        List<TopicReportEntity> expectedTopics = Arrays.asList(topicReport1, topicReport2);
        when(reportRepository.findByChapter(chapterEntity)).thenReturn(expectedTopics);

        Collection<TopicReportEntity> actualTopics = topicReportService.getTopicsByChapter(1L);

        assertEquals(expectedTopics, actualTopics);
        assertEquals(expectedTopics.get(1).getTopic(), topicReport2.getTopic());


    }
}