package com.group.practic.controller;

import static com.group.practic.util.ResponseUtils.getResponse;
import static com.group.practic.util.ResponseUtils.postResponse;

import com.group.practic.dto.PersonDto;
import com.group.practic.entity.PersonEntity;
import com.group.practic.service.PersonService;
import jakarta.validation.constraints.Min;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/persons")
public class PersonController {

    @Autowired
    private PersonService personService;


    @GetMapping("/")
    public ResponseEntity<Collection<PersonEntity>> get(@RequestParam(required = false) String name,
            @RequestParam(required = false) boolean inactive,
            @RequestParam(required = false) boolean ban) {
        if (name == null) {
            return getResponse(personService.get(inactive, ban));
        }
        return getResponse(personService.get(name, inactive, ban));
    }


    @GetMapping("/{id}")
    public ResponseEntity<PersonEntity> get(@Min(1) @PathVariable long id) {
        return getResponse(personService.get(id));
    }

    @GetMapping("/profile")
    public ResponseEntity<PersonEntity> getProfile() {
        PersonEntity foundPerson = personService.getCurrentPerson();

        if (foundPerson == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(foundPerson);
    }

    @PostMapping("/profile")
    public ResponseEntity<PersonEntity> createProfile() {
        PersonEntity createdUser = personService.createUserIfNotExists();

        if (createdUser == null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }


    @PostMapping
    public ResponseEntity<PersonEntity> createCourse(@RequestBody PersonDto personDto) {
        return postResponse(personService.create(personDto));
    }

}
