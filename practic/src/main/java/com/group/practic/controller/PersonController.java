package com.group.practic.controller;

import com.group.practic.dto.ApplicantDto;
import com.group.practic.dto.PersonDto;
import com.group.practic.dto.ProfileDto;
import com.group.practic.entity.RoleEntity;
import com.group.practic.service.*;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.group.practic.util.ResponseUtils.*;


@RestController
@RequestMapping("/api/persons")
public class PersonController {

    PersonService personService;

    ApplicantService applicantService;

    MentorService mentorService;

    StudentService studentService;

    CourseService courseService;

    ProfileService profileService;

    @Autowired
    public PersonController(PersonService personService, CourseService courseService,
                            ApplicantService applicantService, MentorService mentorService,
                            StudentService studentService, ProfileService profileService) {
        this.personService = personService;
        this.courseService = courseService;
        this.applicantService = applicantService;
        this.mentorService = mentorService;
        this.studentService = studentService;
        this.profileService = profileService;
    }


    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<Collection<PersonDto>> getAll() {
        return getResponse(PersonDto.map(personService.get()));
    }


    @GetMapping("/")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<Collection<PersonDto>> get(@RequestParam(required = false) String name,
                                                     @RequestParam(required = false) boolean inactive,
                                                     @RequestParam(required = false) boolean ban) {
        if (name == null) {
            return getResponse(PersonDto.map(personService.get(inactive, ban)));
        }
        return getResponse(PersonDto.map(personService.get(name, inactive, ban)));
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<PersonDto> get(@PathVariable long id) {
        return getResponse(personService.get(id).map(PersonDto::map));
    }


    @PutMapping("/ban/{id}/{ban}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PersonDto> ban(@PathVariable long id, @PathVariable boolean ban) {
        return getResponse(personService.get(id)
                .map(person -> personService.ban(person, ban)));
    }


    @PostMapping("/{id}/add/{newRole}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<PersonDto> addRole(@PathVariable long id, @PathVariable String newRole) {
        RoleEntity role = personService.getRole(newRole);
        return role == null ? badRequest()
                : postResponse(personService.get(id)
                .map(person -> PersonDto.map(personService.addRole(person, role))));
    }


    @PostMapping("/{id}/remove/{newRole}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<PersonDto> removeRole(@PathVariable long id,
                                                @PathVariable String newRole) {
        RoleEntity role = personService.getRole(newRole);
        return role == null ? badRequest()
                : postResponse(personService.get(id)
                .map(person -> PersonDto.map(personService.removeRole(person, role))));
    }


    @GetMapping("/me")
    public ResponseEntity<PersonDto> getCurrentUser() {
        return getResponse(PersonDto.map(personService.getMe()));
    }


    @GetMapping("/application/{id}")
    public ResponseEntity<ApplicantDto> isApplied(@PathVariable long id) {
        return getResponse(applicantService.get(id).map(ApplicantDto::map));
    }


    @PostMapping("/application/{slug}")
    public ResponseEntity<ApplicantDto> applicationForCourse(@PathVariable String slug) {
        return postResponse(courseService.get(slug).map(personService::createApplication));
    }

    @GetMapping("/profile")
    public ResponseEntity<ProfileDto> getProfile() {

        return getResponse(profileService.getProfile(PersonService.me()));
    }

    @PutMapping("/profile")
    public ResponseEntity<ProfileDto> putProfile(@RequestBody ProfileDto profileDto) {

        return updateResponse(profileService.updateProfile(profileDto));
    }


}
