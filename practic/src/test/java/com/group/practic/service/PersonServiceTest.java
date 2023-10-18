package com.group.practic.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.group.practic.dto.AuthUserDto;
import com.group.practic.dto.SignUpRequestDto;
import com.group.practic.entity.PersonEntity;
import com.group.practic.entity.RoleEntity;
import com.group.practic.exception.UserAlreadyExistAuthenticationException;
import com.group.practic.repository.PersonRepository;
import com.group.practic.repository.RoleRepository;
import com.group.practic.security.user.LinkedinOauth2UserInfo;
import com.group.practic.security.user.Oauth2UserInfo;
import jakarta.persistence.EntityExistsException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

class PersonServiceTest {
/*
    @InjectMocks
    private PersonService personService;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    @Test
    void testGetAllPersons() {
        when(personRepository.findAll())
                .thenReturn(Arrays.asList(
                        new PersonEntity("Chloe", "linkedin"),
                        new PersonEntity("Rachel", "linkedin")
                ));

        List<PersonEntity> people = personService.get();
        assertEquals(2, people.size());
        assertEquals("Chloe", people.get(0).getName());
        assertEquals("Rachel", people.get(1).getName());
    }

    @Test
    void testGetPersonById() {
        when(personRepository.findById(1L))
                .thenReturn(Optional.of(new PersonEntity("Max", "linkedin")));

        Optional<PersonEntity> person = personService.get(1);
        assertTrue(person.isPresent());
        assertEquals("Max", person.get().getName());
    }

    @Test
    void testGetPersonByName() {
        when(personRepository.findAllByName("Max"))
                .thenReturn(Optional.of(new PersonEntity("Max", "linkedin")));

        PersonEntity person = personService.get("Max").orElse(null);

        assertNotNull(person);
        assertEquals("Max", person.getName());
    }

    @Test
    void testGetAllInactiveOrBannedPeople() {
        when(personRepository.findAllByInactiveAndBan(false, true))
                .thenReturn(Arrays.asList(
                        new PersonEntity("Chloe", "linkedin"),
                        new PersonEntity("Rachel", "linkedin")
                ));

        List<PersonEntity> people = personService.get(false, true);

        assertEquals(2, people.size());
        assertEquals("Chloe", people.get(0).getName());
        assertEquals("Rachel", people.get(1).getName());
    }

    @Test
    void testGetAllByNameInactiveOrBannedPeople() {
        PersonEntity personEntity = new PersonEntity("Max", "linkedin");
        personEntity.setInactive(false);
        personEntity.setBan(true);

        when(personRepository.findAllByNameAndInactiveAndBan("Max", false, true))
                .thenReturn(List.of(personEntity));

        List<PersonEntity> people = personService.get("Max", false, true);

        assertEquals(1, people.size());
        assertEquals("Max", people.get(0).getName());
        assertTrue(people.get(0).isBan());
        assertFalse(people.get(0).isInactive());
    }

    @Test
    void testGetByDiscord() {
        PersonEntity personEntity = new PersonEntity("Max", "linkedin");
        personEntity.setDiscord("discord");

        when(personRepository.findByDiscord("discord"))
                .thenReturn(Optional.of(personEntity));

        PersonEntity person = personService.getByDiscord("discord").orElse(null);

        assertNotNull(person);
        assertEquals("Max", person.getName());
        assertEquals("discord", person.getDiscord());
    }

    @Test
    void testGetByLinkedin() {
        PersonEntity personEntity = new PersonEntity("Max", "linkedin");

        when(personRepository.findByLinkedin("linkedin"))
                .thenReturn(Optional.of(personEntity));

        PersonEntity person = personService.getByLinkedin("linkedin").orElse(null);

        assertNotNull(person);
        assertEquals("Max", person.getName());
        assertEquals("linkedin", person.getLinkedin());
    }

    @Test
    void testFindUserRolesById() {
        PersonEntity personEntity = new PersonEntity("Max", "linkedin");
        RoleEntity roleUser = new RoleEntity("ROLE_USER");
        personEntity.setRoles(Set.of(roleUser));

        when(personRepository.findById(1L))
                .thenReturn(Optional.of(personEntity));

        Set<RoleEntity> roles = personService.findUserRolesById(1L);

        assertEquals(1, roles.size());
        assertTrue(roles.contains(roleUser));
    }

    @Test
    void testAddAlreadyExistedRoleToUserById() {
        PersonEntity personEntity = new PersonEntity("Max", "linkedin");
        RoleEntity roleUser = new RoleEntity("ROLE_USER");
        personEntity.setRoles(Set.of(roleUser));

        when(personRepository.findById(1L))
                .thenReturn(Optional.of(personEntity));

        assertThrows(EntityExistsException.class,
                () -> personService.addRoleToUserById(1L, "ROLE_USER"));
    }

    @Test
    void testAddNewRoleToUserById() {
        PersonEntity personEntity = new PersonEntity("Max", "linkedin");
        RoleEntity roleUser = new RoleEntity("ROLE_USER");
        personEntity.setRoles(Set.of(roleUser));

        when(personRepository.findById(1L))
                .thenReturn(Optional.of(personEntity));

        PersonEntity savedPersonEntity = new PersonEntity("Max", "linkedin");
        RoleEntity roleMentor = new RoleEntity("ROLE_MENTOR");
        savedPersonEntity.setRoles(Set.of(roleUser, roleMentor));

        when(personRepository.save(personEntity)).thenReturn(savedPersonEntity);

        PersonEntity resultPersonEntity = personService
                .addRoleToUserById(1L, "ROLE_MENTOR").orElse(null);

        assertNotNull(resultPersonEntity);
        assertEquals(2, resultPersonEntity.getRoles().size());
        assertTrue(resultPersonEntity.containsRole("ROLE_MENTOR"));
    }

    @Test
    void testUpdateExistingUser() {

        PersonEntity mustBePersonEntity = new PersonEntity();
        mustBePersonEntity.setName("name surname");
        mustBePersonEntity.setEmail("email");
        mustBePersonEntity.setProfilePictureUrl("pictureUrl");

        when(personRepository.save(any(PersonEntity.class)))
                .thenReturn(mustBePersonEntity);
        Oauth2UserInfo oauth2UserInfo = new LinkedinOauth2UserInfo(Map.of(
                "localizedFirstName", "name",
                "localizedLastName", "surname",
                "emailAddress", "email",
                "pictureUrl", "pictureUrl"
        ));

        PersonEntity personEntity = new PersonEntity();
        PersonEntity updatedPersonEntity =
                personService.updateExistingUser(personEntity, oauth2UserInfo);

        assertEquals(mustBePersonEntity, updatedPersonEntity);
    }

    @Test
    void testRegisterNewUser_UserAlreadyExist() {
        when(personRepository.findPersonEntityByEmail("email"))
                .thenReturn(Optional.of(new PersonEntity()));
        SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder().build();
        signUpRequestDto.setEmail("email");

        assertThrows(UserAlreadyExistAuthenticationException.class,
                () -> personService.registerNewUser(signUpRequestDto));
    }

    @Test
    void testRegisterNewUser_Success() {
        when(personRepository.findPersonEntityByEmail("email"))
                .thenReturn(Optional.empty());
        SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder().build();
        signUpRequestDto.setEmail("email");
        signUpRequestDto.setName("name");
        signUpRequestDto.setPassword("password");
        signUpRequestDto.setProviderUserId("id");
        signUpRequestDto.setProfilePictureUrl("pictureUrl");

        PersonEntity mustBeUser = new PersonEntity();
        mustBeUser.setEmail("email");
        mustBeUser.setName("name");
        mustBeUser.setPassword("password");
        mustBeUser.setLinkedin("id");
        mustBeUser.setProfilePictureUrl("pictureUrl");

        when(personRepository.save(any(PersonEntity.class)))
                .thenReturn(mustBeUser);
        when(roleRepository.findByName("USER")).thenReturn(new RoleEntity("USER"));

        PersonEntity registeredUser = personService.registerNewUser(signUpRequestDto);

        assertEquals(mustBeUser, registeredUser);
    }

    @Test
    void testSaveUser() {
        PersonEntity savedUser = new PersonEntity();

        when(personRepository.save(savedUser))
                .thenReturn(savedUser);

        PersonEntity resultUser = personService.save(savedUser);

        assertEquals(savedUser, resultUser);
    }

    @Test
    void testGetOauth2User() {
        PersonEntity person = new PersonEntity();
        person.setName("name");

        AuthUserDto authUserDto = AuthUserDto.builder().person(person).build();

        when(authentication.getPrincipal()).thenReturn(authUserDto);

        AuthUserDto oauth2User = (AuthUserDto) personService.getOauth2User();

        assertEquals("name", oauth2User.getName());
    }

    @Test
    void getCurrentPerson() {
        PersonEntity person = new PersonEntity();
        person.setLinkedin("linkedin");

        AuthUserDto authUserDto = AuthUserDto.builder()
                .person(person)
                .attributes(Map.of("id", "linkedin"))
                .build();

        when(authentication.getPrincipal()).thenReturn(authUserDto);
        when(personRepository.findByLinkedin("linkedin")).thenReturn(Optional.of(person));

        PersonEntity currentPerson = personService.getCurrentPerson().orElse(null);

        assertNotNull(currentPerson);
        assertEquals("linkedin", currentPerson.getLinkedin());
    }

    @Test
    void testCreateUserIfNotExists() {
        PersonEntity person = new PersonEntity();
        person.setLinkedin("linkedin");
        person.setName("first last");

        AuthUserDto authUserDto = AuthUserDto.builder()
                .person(person)
                .attributes(Map.of("id", "linkedin",
                        "localizedFirstName", "first",
                        "localizedLastName", "last"))
                .build();

        when(authentication.getPrincipal()).thenReturn(authUserDto);
        when(personRepository.findByLinkedin("linkedin")).thenReturn(Optional.empty());
        when(personRepository.save(any(PersonEntity.class))).thenReturn(person);

        PersonEntity currentPerson = personService.createUserIfNotExists();

        assertNotNull(currentPerson);
        assertEquals("linkedin", currentPerson.getLinkedin());
        assertEquals("first last", currentPerson.getName());
    }

    @Test
    void testIsCurrentPersonMentor() {
        PersonEntity person = new PersonEntity();
        person.setLinkedin("linkedin");
        person.setRoles(Set.of(new RoleEntity("MENTOR")));

        AuthUserDto authUserDto = AuthUserDto.builder()
                .attributes(Map.of("id", "linkedin"))
                .build();

        when(authentication.getPrincipal()).thenReturn(authUserDto);
        when(personRepository.findByLinkedin("linkedin")).thenReturn(Optional.of(person));

        boolean isMentor = personService.isCurrentPersonMentor();

        assertTrue(isMentor);
    }

    @Test
    void testAddEmailToCurrentUser() {
        PersonEntity person = new PersonEntity();
        person.setLinkedin("linkedin");
        person.setContacts("email");

        AuthUserDto authUserDto = AuthUserDto.builder()
                .attributes(Map.of("id", "linkedin"))
                .build();

        when(authentication.getPrincipal()).thenReturn(authUserDto);
        when(personRepository.findByLinkedin("linkedin")).thenReturn(Optional.of(person));
        when(personRepository.save(any(PersonEntity.class))).thenReturn(person);

        PersonEntity updatedPerson = personService.addEmailToCurrentUser("email").orElse(null);

        assertNotNull(updatedPerson);
        assertEquals("email", updatedPerson.getContacts());
    }
    */
}