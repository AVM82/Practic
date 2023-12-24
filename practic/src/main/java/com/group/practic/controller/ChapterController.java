package com.group.practic.controller;

import static com.group.practic.util.ResponseUtils.getResponse;

import com.group.practic.dto.ChapterDto;
import com.group.practic.entity.ChapterEntity;
import com.group.practic.service.ChapterService;
import com.group.practic.service.PersonService;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/chapters")
public class ChapterController {

    ChapterService chapterService;


    @Autowired
    public ChapterController(ChapterService chapterService) {
        this.chapterService = chapterService;
    }


    @GetMapping("/{id}")
    public ResponseEntity<ChapterDto> get(@Min(1) @PathVariable long id) {
        return getResponse(chapterService.get(id)
                .map(chapter -> ChapterDto.map(chapter, !PersonService.hasAdvancedRole())));
    }


    @GetMapping("/shortName/{shortName}")
    public ResponseEntity<ChapterEntity> getByShortName(@PathVariable String shortName) {
        return getResponse(chapterService.getByShortName(shortName));
    }

}
