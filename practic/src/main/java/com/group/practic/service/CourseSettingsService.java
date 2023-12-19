package com.group.practic.service;

import com.group.practic.entity.CourseEntity;
import com.group.practic.entity.CourseSettingsEntity;
import com.group.practic.repository.CourseSettingsRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseSettingsService {
    private final CourseSettingsRepository courseSettingsRepository;

    public Optional<CourseSettingsEntity> getByCourse(CourseEntity course) {
        return courseSettingsRepository.findByCourse(course);
    }
}
