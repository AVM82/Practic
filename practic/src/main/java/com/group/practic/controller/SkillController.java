package com.group.practic.controller;

import com.group.practic.dto.SkillDto;
import com.group.practic.entity.SkillEntity;
import com.group.practic.service.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/skills")
@RequiredArgsConstructor
public class SkillController {

    private final SkillService skillService;

    @GetMapping
    public ResponseEntity<List<SkillEntity>> getAllSkills() {
        return ResponseEntity.ok(skillService.findAll());
    }


    @GetMapping("/{name}")
    public ResponseEntity<SkillEntity> getSkillByName(@PathVariable String name) {
        return new ResponseEntity<>(skillService.findByName(name), HttpStatus.FOUND);
    }

    @PostMapping
    public ResponseEntity<SkillEntity> createSkill(@RequestBody SkillDto skillDto) {
        return new ResponseEntity<>(skillService.save(skillDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<SkillEntity> deleteSkill(@PathVariable String name) {
        return new ResponseEntity<>(skillService.delete(name), HttpStatus.OK);
    }

    @PutMapping("/{skillName}")
    public ResponseEntity<SkillEntity> addSkillToChapter(@RequestBody long chapterId, @PathVariable String skillName) {
        return new ResponseEntity<>(skillService.addSkillToChapter(skillName, chapterId), HttpStatus.OK);
    }

}
