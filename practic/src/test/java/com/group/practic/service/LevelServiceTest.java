//package com.group.practic.service;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertSame;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//
//import com.group.practic.PropertyLoader;
//import com.group.practic.entity.CourseEntity;
//import com.group.practic.entity.LevelEntity;
//import com.group.practic.repository.LevelRepository;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//import java.util.Properties;
//import java.util.Set;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//
//
//class LevelServiceTest {
//
//    @InjectMocks
//    private LevelService levelService;
//
//    @Mock
//    private LevelRepository levelRepository;
//
//    @Mock
//    private PropertyLoader propertyLoader;
//
//    @BeforeEach
//    public void setup() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testGetAllLevels() {
//        CourseEntity course = new CourseEntity();
//        List<LevelEntity> levelList = new ArrayList<>();
//        when(levelRepository.findAllByCourseOrderByNumberAsc(course)).thenReturn(levelList);
//
//        List<LevelEntity> result = levelService.getAll(course);
//
//        assertSame(levelList, result);
//    }
//
//    @Test
//    void testSetLevel() {
//        CourseEntity course = new CourseEntity();
//        int number = 1;
//        List<Integer> chapterN = Arrays.asList(1, 2, 3);
//        LevelEntity existingLevel = new LevelEntity(1, course, number, chapterN);
//        when(levelRepository.findByCourseAndNumber(course, number))
//                .thenReturn(Optional.of(existingLevel));
//
//        when(levelRepository.save(existingLevel)).thenReturn(existingLevel);
//
//        LevelEntity result = levelService.set(course, number, chapterN);
//
//        assertSame(existingLevel, result);
//    }
//
//    @Test
//    void testCreateLevel() {
//        LevelEntity level = new LevelEntity(1, new CourseEntity(), 1, Arrays.asList(1, 2, 3));
//
//        when(levelRepository.save(level)).thenReturn(level);
//
//        LevelEntity result = levelService.create(level);
//
//        assertSame(level, result);
//    }
//
//    @Test
//    void testGetLevelsSet() {
//
//        Properties properties = new Properties();
//        properties.setProperty("level1", "1,2,3");
//        properties.setProperty("other.property", "Value");
//
//        when(propertyLoader.getEntrySet()).thenReturn(properties.entrySet());
//        LevelEntity level1 = new LevelEntity();
//
//        CourseEntity course = new CourseEntity();
//        level1.setCourse(new CourseEntity());
//        when(levelRepository.save(any())).thenReturn(level1);
//
//        Set<LevelEntity> result = levelService.getLevelsSet(course, propertyLoader);
//
//        assertEquals(1, result.size());
//    }
//}