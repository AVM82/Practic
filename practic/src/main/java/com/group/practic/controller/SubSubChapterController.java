package com.group.practic.controller;

import static com.group.practic.util.ResponseUtils.getResponse;

import com.group.practic.entity.SubSubChapterEntity;
import com.group.practic.service.ChapterService;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/subsubchapter")
public class SubSubChapterController {

    @Autowired
    ChapterService chapterService;


    @GetMapping("/{id}")
    public ResponseEntity<SubSubChapterEntity> get(@Min(1) @PathVariable long id) {
        return getResponse(chapterService.getSubSub(id));
    }

}
