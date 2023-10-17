package com.group.practic.metrics;

import com.group.practic.entity.ChapterEntity;
import com.group.practic.repository.ChapterRepository;
import com.group.practic.repository.StudentOnCourseRepository;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class NumberActiveStudentsCustomMetric {

    MeterRegistry meterRegistry;

    StudentOnCourseRepository studentOnCourseRepository;

    ChapterRepository chapterRepository;


    @Autowired
    public NumberActiveStudentsCustomMetric(MeterRegistry meterRegistry,
            StudentOnCourseRepository studentOnCourseRepository,
            ChapterRepository chapterRepository) {
        this.meterRegistry = meterRegistry;
        this.studentOnCourseRepository = studentOnCourseRepository;
        this.chapterRepository = chapterRepository;
    }


    @PostConstruct
    private void createMetricsForAllChapters() {
        for (ChapterEntity chapter : chapterRepository.findAll()) {
            String description = String.format(
                    "Custom metric: Number of active student by chapter №%d %s. On course: %s",
                    chapter.getNumber(), chapter.getShortName(), chapter.getCourse().getName());
            Gauge.builder("number_active_student_by_chapter_" + chapter.getNumber(), this,
                    instance -> calculateNumberStudent(chapter.getNumber()))
                    .description(description).register(meterRegistry);
        }
    }


    private int calculateNumberStudent(int numbChapter) {
        return studentOnCourseRepository
                .findAllByBanFalseAndInactiveFalseAndActiveChapter_Number(numbChapter).size();
    }

}
