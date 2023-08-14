package com.group.practic.controller;

import static com.group.practic.util.ResponseUtils.getResponse;
import static com.group.practic.util.ResponseUtils.postResponse;

import com.group.practic.entity.ChapterEntity;
import com.group.practic.entity.SubChapterEntity;
import com.group.practic.service.ChapterService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/chapter")
public class ChapterController {

    @Autowired
    ChapterService chapterService;


    @GetMapping("/{id}")
    public ResponseEntity<ChapterEntity> get(@Min(1) @PathVariable long id) {
        return getResponse(chapterService.get(id));
    }


    @PostMapping()
    public ResponseEntity<ChapterEntity> create(@Min(1) @RequestParam long courseId,
            @RequestBody @NotBlank String name, @RequestParam(required = false) Integer number) {
        return postResponse(chapterService.create(courseId, number == null ? 0 : number, name));
    }


    @PostMapping("/{chapterId}")
    public ResponseEntity<SubChapterEntity> createSub(@Min(1) @PathVariable long chapterId,
            @RequestBody @NotBlank String name, @RequestParam(required = false) int number) {
        return postResponse(chapterService.createSub(chapterId, number, name));
    }


    @PostMapping("/subChapter/{id}")
    public ResponseEntity<SubChapterEntity> createSubSub(@Min(1) @PathVariable long subChapterId,
            @RequestBody @NotBlank String name, @RequestParam(required = false) int number) {
        return postResponse(chapterService.createSub(subChapterId, number, name));
    }


}