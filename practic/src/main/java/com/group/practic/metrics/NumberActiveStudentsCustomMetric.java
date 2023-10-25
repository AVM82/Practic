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

    private int calculateNumberStudent(int numbChapter) {
        return studentOnCourseRepository
                .findAllByBanFalseAndInactiveFalseAndActiveChapter_Number(numbChapter).size();
    }

}
