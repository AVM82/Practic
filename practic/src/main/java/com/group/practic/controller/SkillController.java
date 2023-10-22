package com.group.practic.controller;

import static com.group.practic.util.ResponseUtils.deleteResponse;
import static com.group.practic.util.ResponseUtils.getResponse;
import static com.group.practic.util.ResponseUtils.postResponse;
import static com.group.practic.util.ResponseUtils.updateResponse;

import com.group.practic.dto.SkillDto;
import com.group.practic.entity.SkillEntity;
import com.group.practic.service.SkillService;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/skills")
public class SkillController {

    final SkillService skillService;


    @Autowired
    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }


    @GetMapping
    public ResponseEntity<Collection<SkillEntity>> getAllSkills() {
        return getResponse(skillService.findAll());
    }


    @GetMapping("/{name}")
    public ResponseEntity<SkillEntity> getSkillByName(@PathVariable String name) {
        return getResponse(skillService.findByName(name));
    }


    @PostMapping
    public ResponseEntity<SkillEntity> createSkill(@RequestBody SkillDto skillDto) {
        return postResponse(skillService.save(skillDto));
    }


    @DeleteMapping("/{name}")
    public ResponseEntity<SkillEntity> deleteSkill(@PathVariable String name) {
        return deleteResponse(skillService.delete(name));
    }


    @PutMapping("/{name}")
    public ResponseEntity<SkillEntity> addSkillToChapter(@RequestParam long chapterId,
            @PathVariable String name) {
        return updateResponse(skillService.addSkillToChapter(name, chapterId));
    }

}
