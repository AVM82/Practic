package com.group.practic.service;

import com.group.practic.entity.CourseEntity;
import com.group.practic.entity.LevelEntity;
import com.group.practic.repository.LevelRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class LevelService {

    LevelRepository levelRepository;


    @Autowired
    public LevelService(LevelRepository levelRepository) {
        this.levelRepository = levelRepository;
    }


    public List<LevelEntity> getAll(CourseEntity course) {
        return levelRepository.findAllByCourseOrderByNumberAsc(course);
    }


    public LevelEntity set(CourseEntity course, int number, List<Integer> chapterN) {
        Optional<LevelEntity> level = levelRepository.findByCourseAndNumber(course, number);
        if (level.isPresent()) {
            level.get().setChapterN(chapterN);
            return levelRepository.save(level.get());
        }
        return levelRepository.save(new LevelEntity(0, course, number, chapterN));
    }


    public LevelEntity create(LevelEntity level) {
        return set(level.getCourse(), level.getNumber(), level.getChapterN());
    }

}
