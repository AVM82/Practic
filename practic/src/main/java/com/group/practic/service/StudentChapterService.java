package com.group.practic.service;

import com.group.practic.entity.ChapterEntity;
import com.group.practic.entity.ChapterPartEntity;
import com.group.practic.entity.PersonEntity;
import com.group.practic.entity.StudentChapterEntity;
import com.group.practic.entity.StudentOnCourseEntity;
import com.group.practic.entity.StudentPracticeEntity;
import com.group.practic.exception.ResourceNotFoundException;
import com.group.practic.repository.StudentChapterRepository;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentChapterService {

    private final StudentChapterRepository studentChapterRepository;

    private final ChapterService chapterService;

    private final StudentOnCourseService studentOnCourseService;

    private final ChapterPartService chapterPartService;

    private final StudentPracticeService studentPracticeService;

    @Autowired
    public StudentChapterService(
            StudentChapterRepository studentChapterRepository,
            ChapterService chapterService, StudentOnCourseService studentOnCourseService,
            ChapterPartService chapterPartService, StudentPracticeService studentPracticeService) {
        this.studentChapterRepository = studentChapterRepository;
        this.chapterService = chapterService;
        this.studentOnCourseService = studentOnCourseService;
        this.chapterPartService = chapterPartService;
        this.studentPracticeService = studentPracticeService;
    }

    public Set<StudentChapterEntity> findOpenChapters(long studentId) {
        return studentChapterRepository.findByStudentId(studentId);
    }

    public StudentChapterEntity addChapter(long studentId, long chapterId) {
        Optional<ChapterEntity> chapter = chapterService.get(chapterId);
        StudentOnCourseEntity student = studentOnCourseService.get(studentId);

        if (chapter.isPresent() && student != null) {
            StudentChapterEntity studentChapter = new StudentChapterEntity();
            studentChapter.setStudent(student.getStudent());
            studentChapter.setChapter(chapter.get());
            addPractices(studentId, chapterId);
            return studentChapterRepository.saveAndFlush(studentChapter);
        } else if (chapter.isEmpty()) {
            throw new ResourceNotFoundException("Chapter ", "chapterId",  chapterId);
        } else {
            throw new ResourceNotFoundException("Student ", "studentId",  studentId);
        }
    }

    public StudentChapterEntity update(StudentChapterEntity studentChapter) {
        return studentChapterRepository.saveAndFlush(studentChapter);
    }

    public Set<StudentPracticeEntity> addPractices(long studentId, long chapterId) {
        Optional<Set<ChapterPartEntity>> practices = chapterPartService.getAllPractices(chapterId);
        Optional<ChapterEntity> chapter = chapterService.get(chapterId);
        PersonEntity student = studentOnCourseService.get(studentId).getStudent();

        return practices.map(chapterPartEntities -> chapterPartEntities.stream()
                .map(chapterPart ->
                        studentPracticeService.addPractice(student, chapterPart, chapter.get()))
                .collect(Collectors.toUnmodifiableSet())).orElseGet(HashSet::new);

    }
}
