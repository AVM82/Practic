package com.group.practic.controller;

import com.group.practic.dto.StudentPracticeDto;
import com.group.practic.entity.ChapterEntity;
import com.group.practic.entity.ChapterPartEntity;
import com.group.practic.entity.PersonEntity;
import com.group.practic.entity.StudentPracticeEntity;
import com.group.practic.enumeration.PracticeState;
import com.group.practic.service.ChapterPartService;
import com.group.practic.service.ChapterService;
import com.group.practic.service.PersonService;
import com.group.practic.service.StudentChapterService;
import com.group.practic.service.StudentPracticeService;
import com.group.practic.util.Converter;
import jakarta.validation.Valid;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mentor")
public class MentorController {

    private ChapterPartService chapterPartService;

    private PersonService personService;

    private StudentPracticeService studentPracticeService;

    private ChapterService chapterService;

    private StudentChapterService studentChapterService;

    @Autowired
    public MentorController(ChapterPartService chapterPartService, PersonService personService,
                            StudentPracticeService studentPracticeService,
                            ChapterService chapterService,
                            StudentChapterService studentChapterService) {
        this.chapterPartService = chapterPartService;
        this.personService = personService;
        this.studentPracticeService = studentPracticeService;
        this.chapterService = chapterService;
        this.studentChapterService = studentChapterService;
    }

    @PostMapping("/practices")
    public ResponseEntity<StudentPracticeDto> approvePractice(
            @RequestBody @Valid StudentPracticeDto studentPracticeDto
    ) {
        Optional<PersonEntity> student = personService.get(studentPracticeDto.getStudentId());

        if (student.isPresent()) {
            ChapterPartEntity chapterPart =
                    chapterPartService.getChapterPartById(studentPracticeDto.getChapterPartId());

            StudentPracticeEntity studentPractice =
                    studentPracticeService.getPractice(
                            student.get(), studentPracticeDto.getChapterPartId()
                    );

            studentPractice.setState(PracticeState.APPROVED);

            StudentPracticeEntity updatedPractice = studentPracticeService.save(studentPractice);

            Set<StudentPracticeEntity> practices =
                    studentPracticeService.getAllPracticesByChapter(
                            student.get(), chapterPart.getChapter()
                    );

            ChapterEntity nextChapter = getNextChapter(chapterPart.getChapter());

            if (isAllPracticesApproved(practices) && nextChapter != null) {
                studentChapterService.addChapter(student.get().getId(), nextChapter.getId());
            }

            return ResponseEntity.ok(Converter.convert(updatedPractice));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }

    private ChapterEntity getNextChapter(ChapterEntity chapter) {
        Optional<ChapterEntity> nextChapter =
                chapterService.getChapterByNumber(chapter.getCourse(), chapter.getNumber() + 1);
        return nextChapter.orElse(null);
    }

    private boolean isAllPracticesApproved(Set<StudentPracticeEntity> practices) {
        return practices.stream()
                .allMatch(practice -> practice.getState() == PracticeState.APPROVED);
    }
}
