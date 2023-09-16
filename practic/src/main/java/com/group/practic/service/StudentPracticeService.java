package com.group.practic.service;

import com.group.practic.entity.ChapterEntity;
import com.group.practic.entity.ChapterPartEntity;
import com.group.practic.entity.PersonEntity;
import com.group.practic.entity.StudentPracticeEntity;
import com.group.practic.enumeration.PracticeState;
import com.group.practic.repository.StudentPracticeRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentPracticeService {

    private final StudentPracticeRepository studentPracticeRepository;

    private final ChapterPartService chapterPartService;


    @Autowired
    public StudentPracticeService(StudentPracticeRepository studentPracticeRepository,
                                  ChapterPartService chapterPartService) {
        this.studentPracticeRepository = studentPracticeRepository;
        this.chapterPartService = chapterPartService;
    }

    public List<StudentPracticeEntity> getAllStudentsByState(PracticeState state) {
        return studentPracticeRepository.findByState(state);
    }

    public StudentPracticeEntity addPractice(
            PersonEntity student,
            ChapterPartEntity chapterPart,
            ChapterEntity chapter
    ) {
        StudentPracticeEntity practice = new StudentPracticeEntity();
        practice.setStudent(student);
        practice.setChapterPart(chapterPart);
        practice.setState(PracticeState.NOT_STARTED);
        practice.setChapter(chapter);
        return  studentPracticeRepository.save(practice);
    }

    public StudentPracticeEntity getPractice(PersonEntity student, long chapterPartId) {
        ChapterPartEntity chapterPart = chapterPartService.getChapterPartById(chapterPartId);
        return studentPracticeRepository.findByStudentAndChapterPart(student, chapterPart);
    }

    public StudentPracticeEntity save(StudentPracticeEntity studentPractice) {
        return studentPracticeRepository.save(studentPractice);
    }
}
