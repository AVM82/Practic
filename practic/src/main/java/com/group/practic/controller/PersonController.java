package com.group.practic.controller;

import static com.group.practic.util.ResponseUtils.getResponse;
import static com.group.practic.util.ResponseUtils.postResponse;

import com.group.practic.dto.PersonApplyOnCourseDto;
import com.group.practic.dto.PersonDto;
import com.group.practic.entity.PersonEntity;
import com.group.practic.entity.RoleEntity;
import com.group.practic.service.PersonApplicationService;
import com.group.practic.service.PersonService;
import jakarta.validation.constraints.Min;
import java.util.Collection;
import java.util.List;
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

    private final PersonService personService;
    private final PersonApplicationService personApplicationService;


    @Autowired
    public PersonController(PersonService personService,
                            PersonApplicationService personApplicationService) {
        this.personService = personService;
        this.personApplicationService = personApplicationService;
    }


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
        return getResponse(personService.getCurrentPerson());
    }


    @PostMapping("/profile")
    public ResponseEntity<PersonEntity> createProfile(@RequestParam String email) {
        return postResponse(personService.addEmailToCurrentUser(email));
    }


    @PostMapping
    @PreAuthorize("hasRole('ADMIN')||hasRole('MENTOR')")
    public ResponseEntity<PersonEntity> createCourse(@RequestBody PersonDto personDto) {
        return postResponse(personService.create(personDto));
    }


    @GetMapping("/{id}/roles")

    public ResponseEntity<Collection<RoleEntity>> findAllRoles(@PathVariable long id) {
        return getResponse(personService.findUserRolesById(id));

    }


    @PostMapping("/{id}/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PersonEntity> addRole(@PathVariable long id,
            @RequestParam String newRole) {
        return postResponse(personService.addRoleToUserById(id, newRole));
    }


    @GetMapping("/me")
    public ResponseEntity<PersonEntity> getCurrentUser() {
        PersonEntity person = (PersonEntity) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        return ResponseEntity.ok(person);

    }


    @PostMapping("/application/{slug}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PersonEntity> applyOnCourse(
            @PathVariable(value = "slug") String slug) {
        PersonEntity person = getPrincipal();

        if (person != null) {
            return ResponseEntity.ok(
                    personApplicationService.addPersonApplication(person, slug)
            );
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/applicants")
    @PreAuthorize("hasRole('ADMIN') || hasRole('MENTOR')")
    public ResponseEntity<List<PersonApplyOnCourseDto>> personApplication() {
        return ResponseEntity.ok(
                personApplicationService.getNotApplyPerson()
        );
    }

    public PersonEntity getPrincipal() {
        return (PersonEntity) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
    }
}
