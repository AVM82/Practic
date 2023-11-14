package com.group.practic.service;

import com.group.practic.PropertyLoader;
import com.group.practic.entity.CourseEntity;
import com.group.practic.entity.LevelEntity;
import com.group.practic.repository.LevelRepository;
import com.group.practic.util.PropertyUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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


    public List<LevelEntity> getLevelsSet(CourseEntity course, PropertyLoader prop) {
        List<LevelEntity> result = new ArrayList<>();
        Map<Integer, List<Integer>> levelMap = getLevelMap(prop);
        for (Entry<Integer, List<Integer>> entry : levelMap.entrySet()) {
            LevelEntity level = new LevelEntity(0, course, entry.getKey(), entry.getValue());
            result.add(create(level));
        }
        return result;
    }


    Map<Integer, List<Integer>> getLevelMap(PropertyLoader prop) {
        Map<Integer, List<Integer>> result = new HashMap<>();
        int n;
        for (Entry<Object, Object> entry : prop.getEntrySet()) {
            String key = (String) entry.getKey();
            if (key.startsWith(PropertyUtil.LEVEL_KEY) && (n = PropertyUtil
                    .getNumber(key.substring(PropertyUtil.LEVEL_KEY_LENGTH))) != 0) {
                List<Integer> chapterList = new ArrayList<>();
                result.put(n, chapterList);
                for (String chapterN : ((String) entry.getValue()).split(",")) {
                    if ((n = PropertyUtil.getNumber(chapterN)) > 0) {
                        chapterList.add(n);
                    } else {
                        return Collections.emptyMap();
                    }
                }
            }
        }
        return result;
    }

}
