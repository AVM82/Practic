package com.group.practic.service;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.group.practic.entity.CourseEntity;
import com.group.practic.entity.LevelEntity;
import com.group.practic.repository.LevelRepository;

@Service
public class LevelService {
	
	@Autowired
	LevelRepository levelRepository;

	public List<LevelEntity> createMany(CourseEntity course, Map<Integer, List<Integer>> levels) {
		for (Entry<Integer, List<Integer>> entry : levels.entrySet()) {
			LevelEntity level = new LevelEntity(0, course, entry.getKey(), null);
			levelRepository.save(level);
		}
		return levelRepository.findAllByCourse(course);
	}
}
