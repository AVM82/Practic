package com.group.practic.controller;

import com.group.practic.entity.ChapterEntity;
import com.group.practic.service.ChapterService;
import com.group.practic.util.ResponseUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("chapter")
public class ChapterController {

    ChapterService chapterService;

    @Autowired
    ChapterController(ChapterService chapterService) {
        this.chapterService = chapterService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChapterEntity> get(@Valid @Min(1) @PathVariable int id) {
        return ResponseUtils.getResponse(chapterService.get(id));
    }

}
