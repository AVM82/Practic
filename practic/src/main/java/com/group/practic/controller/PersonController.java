package com.group.practic.controller;

import static com.group.practic.util.ResponseUtils.getResponse;
import static com.group.practic.util.ResponseUtils.postResponse;

import com.group.practic.dto.PersonDto;
import com.group.practic.entity.PersonEntity;
import com.group.practic.entity.RoleEntity;
import com.group.practic.service.PersonService;
import jakarta.validation.constraints.Min;
import java.util.Collection;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public ResponseEntity<PersonEntity> createProfile(@RequestParam String email) {
        return new ResponseEntity<>(personService.addEmailToCurrentUser(email), HttpStatus.CREATED);
    }


    @PostMapping
    @PreAuthorize("hasRole('ADMIN')||hasRole('MENTOR')")
    public ResponseEntity<PersonEntity> createCourse(@RequestBody PersonDto personDto) {
        return postResponse(personService.create(personDto));
    }


    @GetMapping("/{id}/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Set<RoleEntity>> findAllRoles(@PathVariable long id) {
        return ResponseEntity.ok(personService.findUserRolesById(id));
    }

    @PostMapping("/{id}/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PersonEntity> addRole(@PathVariable long id,
                                                @RequestParam String newRole) {
        PersonEntity personEntity = personService.addRoleToUserById(id, newRole);

        if (personEntity == null) {
            return ResponseEntity.notFound().build();
        }

        return new ResponseEntity<>(personEntity, HttpStatus.CREATED);
    }

    @GetMapping("/me")
    public ResponseEntity<PersonEntity> getCurrentUser() {
        PersonEntity person = (PersonEntity) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        return ResponseEntity.ok(person);
    }
}
