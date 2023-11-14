package com.group.practic.service;

import com.group.practic.dto.StudentPracticeDto;
import com.group.practic.entity.ChapterEntity;
import com.group.practic.entity.ChapterPartEntity;
import com.group.practic.entity.StudentEntity;
import com.group.practic.entity.StudentPracticeEntity;
import com.group.practic.enumeration.PracticeState;
import com.group.practic.repository.StudentPracticeRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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


    public Set<StudentPracticeEntity> getAllPracticesByChapter(StudentEntity student,
            ChapterEntity chapter) {
        return Set.of();
        /*
         * chapter.getParts().stream() .map(chapterPart ->
         * studentPracticeRepository.findByStudentAndChapterPart(student, chapterPart))
         * .collect(Collectors.toSet());
         */
    }


    public StudentPracticeEntity addPractice(StudentEntity student, ChapterPartEntity chapterPart) {
        StudentPracticeEntity practice = null;
        return studentPracticeRepository.save(practice);
    }


    public StudentPracticeEntity getPractice(StudentEntity student, long chapterPartId) {
        ChapterPartEntity chapterPart = chapterPartService.getChapterPartById(chapterPartId);
        return null;
        /*
         * studentPracticeRepository.findByStudentAndChapterPart(student, chapterPart);
         */
    }


    public StudentPracticeEntity save(StudentPracticeEntity studentPractice) {
        return studentPracticeRepository.save(studentPractice);
    }


    public Set<StudentPracticeEntity> getAllPracticesByStudent() {

        return Set.of();
    }


    public Optional<StudentPracticeEntity> changeState(StudentPracticeDto studentPracticeDto) {
        Optional<StudentPracticeEntity> practice =
                studentPracticeRepository.findById(studentPracticeDto.getId());
        if (practice.isPresent() && practice.get()
                .setNewState(PracticeState.fromString(studentPracticeDto.getState()))) {
            practice = Optional.of(studentPracticeRepository.save(practice.get()));
        }
        return practice;
    }

}
