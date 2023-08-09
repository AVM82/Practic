package com.group.practic.controller;

import com.group.practic.dto.PersonDto;
import com.group.practic.entity.PersonEntity;
import com.group.practic.service.PersonService;
import com.group.practic.util.ResponseUtils;
import java.util.Collection;
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
@RequestMapping("/person")
public class PersonController {

  @Autowired
  private PersonService personService;

  
  @GetMapping("/")
  public ResponseEntity<Collection<PersonEntity>> get(
      @RequestParam(required = false) String name,
      @RequestParam(required = false) Boolean inactive,
      @RequestParam(required = false) Boolean ban) {
    if (name == null) {
      return ResponseUtils.getResponse(personService.get(inactive, ban));
    }
    return ResponseUtils.getResponse(personService.get(name, inactive, ban));
  }

  
  @GetMapping("/{id}")
  public ResponseEntity<PersonEntity> get(@PathVariable long id) {
    return ResponseUtils.getResponse(personService.get(id));
  }
  
  
  @PostMapping
  public ResponseEntity<PersonEntity> createCourse(@RequestBody PersonDto personDto) {
    return ResponseUtils.postResponse(personService.create(personDto));
  }
  
}
