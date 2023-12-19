package com.group.practic.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.group.practic.dto.AuthUserDto;
import com.group.practic.dto.SignUpRequestDto;
import com.group.practic.entity.PersonEntity;
import com.group.practic.entity.RoleEntity;
import com.group.practic.exception.ResourceNotFoundException;
import com.group.practic.repository.PersonRepository;
import com.group.practic.repository.RoleRepository;
import com.group.practic.security.user.LinkedinOauth2UserInfo;
import com.group.practic.security.user.Oauth2UserInfo;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;


@Slf4j
class PersonServiceTest {

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
                        new PersonEntity("Chloe", "linkedin", new RoleEntity()),
                        new PersonEntity("Rachel", "linkedin", new RoleEntity())
                ));

        List<PersonEntity> people = personService.get();
        assertEquals(2, people.size());
        assertEquals("Chloe", people.get(0).getName());
        assertEquals("Rachel", people.get(1).getName());
    }

    @Test
    void testGetPersonById() {
        when(personRepository.findById(1L))
                .thenReturn(Optional.of(
                        new PersonEntity("Max", "linkedin", new RoleEntity())));

        Optional<PersonEntity> person = personService.get(1);
        assertTrue(person.isPresent());
        assertEquals("Max", person.get().getName());
    }

    @Test
    void testGetPersonByName() {
        when(personRepository.findAllByName("Max"))
                .thenReturn(List.of(new PersonEntity("Max", "linkedin", new RoleEntity())));

        List<PersonEntity> persons = personService.get("Max");

        assertFalse(persons.isEmpty());
        assertEquals("Max", persons.get(0).getName());
    }

    @Test
    void testGetAllInactiveOrBannedPeople() {
        when(personRepository.findAllByInactiveAndBan(false, true))
                .thenReturn(Arrays.asList(
                        new PersonEntity("Chloe", "linkedin", new RoleEntity()),
                        new PersonEntity("Rachel", "linkedin", new RoleEntity())
                ));

        List<PersonEntity> people = personService.get(false, true);

        assertEquals(2, people.size());
        assertEquals("Chloe", people.get(0).getName());
        assertEquals("Rachel", people.get(1).getName());
    }

    @Test
    void testGetAllByNameInactiveOrBannedPeople() {
        PersonEntity personEntity = new PersonEntity("Max", "linkedin", new RoleEntity());
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
        PersonEntity personEntity = new PersonEntity("Max", "linkedin", new RoleEntity());
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
        PersonEntity personEntity = new PersonEntity("Max", "linkedin", new RoleEntity());

        when(personRepository.findByLinkedin("linkedin"))
                .thenReturn(Optional.of(personEntity));

        PersonEntity person = personService.getByLinkedin("linkedin").orElse(null);

        assertNotNull(person);
        assertEquals("Max", person.getName());
        assertEquals("linkedin", person.getLinkedin());
    }

    @Test
    void testFindSetRolesByRolesName() {
        RoleEntity role1 = new RoleEntity("ROLE_USER");
        RoleEntity role2 = new RoleEntity("ROLE_ADMIN");

        when(roleRepository.findByName("ROLE_USER")).thenReturn(role1);
        when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(role2);

        Set<RoleEntity> roles = personService.getRoles("ROLE_USER", "ROLE_ADMIN");

        assertEquals(2, roles.size());
        assertTrue(roles.contains(role1));
        assertTrue(roles.contains(role2));
    }
/*
    @Test
    void testAddAlreadyExistedRoleToUserById() {
        PersonEntity personEntity = new PersonEntity("Max", "linkedin", new RoleEntity());
        RoleEntity roleUser = new RoleEntity("ROLE_USER");
        personEntity.setRoles(Set.of(roleUser));

        when(roleRepository.findByName("ROLE_USER")).thenReturn(roleUser);
        when(personRepository.save(any(PersonEntity.class))).thenReturn(personEntity);

        PersonEntity person = personService.addRole(personEntity, "ROLE_USER");

        assertEquals(1, person.getRoles().size());
    }

    @Test
    void testAddNewRoleToUserById() {
        PersonEntity personEntity = new PersonEntity("Max", "linkedin", new RoleEntity());
        RoleEntity roleUser = new RoleEntity("ROLE_USER");
        personEntity.setRoles(Set.of(roleUser));

        PersonEntity savedPersonEntity = new PersonEntity("Max", "linkedin", new RoleEntity());
        RoleEntity roleMentor = new RoleEntity("ROLE_MENTOR");
        savedPersonEntity.setRoles(Set.of(roleUser, roleMentor));

        when(roleRepository.findByName("ROLE_MENTOR")).thenReturn(roleMentor);
        when(personRepository.save(personEntity)).thenReturn(savedPersonEntity);

        PersonEntity resultPersonEntity = personService.addRole(personEntity, "ROLE_MENTOR");

        assertNotNull(resultPersonEntity);
        assertEquals(2, resultPersonEntity.getRoles().size());
        assertTrue(resultPersonEntity.getRoles().contains(roleMentor));
    }
*/
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
        PersonEntity person = new PersonEntity();
        person.setEmail("email");
        when(personRepository.save(any(PersonEntity.class)))
                .thenReturn(person);
        SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder().build();
        signUpRequestDto.setEmail("email");

        PersonEntity savedPerson = personService.registerNewUser(signUpRequestDto);

        assertEquals(person, savedPerson);
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
    void getCurrentPerson() {
        PersonEntity person = new PersonEntity();
        person.setLinkedin("linkedin");
        person.setBan(true);

        when(authentication.getPrincipal()).thenReturn(person);

        PersonEntity currentPerson = personService.getMe();

        assertNotNull(currentPerson);
        assertEquals("linkedin", currentPerson.getLinkedin());
        assertTrue(currentPerson.isBan());
    }

    @Test
    void testCreateUserIfNotExists() {
        PersonEntity person = new PersonEntity();
        person.setLinkedin("linkedin");
        person.setName("first last");

        AuthUserDto authUserDto = AuthUserDto.builder()
                .name("first last")
                .attributes(Map.of("id", "linkedin",
                        "localizedFirstName", "first",
                        "localizedLastName", "last"))
                .build();

        when(authentication.getPrincipal()).thenReturn(authUserDto);
        when(personRepository.save(any(PersonEntity.class))).thenReturn(person);

        PersonEntity currentPerson = personService.createUserIfNotExists();

        assertNotNull(currentPerson);
        assertEquals("linkedin", currentPerson.getLinkedin());
        assertEquals("first last", currentPerson.getName());
    }

    /* @Test
     void testIsCurrentPersonMentorWhenCurrentUserIsMentor() {

         Set<RoleEntity> roles = new HashSet<>();
         roles.add(new RoleEntity(PersonService.ROLE_MENTOR));

         PersonEntity currentPerson = new PersonEntity();
         currentPerson.setRoles(roles);

         Authentication authentication = mock(Authentication.class);
         when(authentication.getPrincipal()).thenReturn(currentPerson);

         SecurityContext securityContext = mock(SecurityContext.class);
         when(securityContext.getAuthentication()).thenReturn(authentication);
         SecurityContextHolder.setContext(securityContext);

         boolean result = personService.isCurrentPersonMentor();
         assertTrue(result);
     }

     @Test
     void testIsCurrentPersonMentorWhenCurrentUserIsNotMentor() {
         Set<RoleEntity> roles = new HashSet<>();
         roles.add(new RoleEntity(PersonService.ROLE_STUDENT));

         PersonEntity currentPerson = new PersonEntity();
         currentPerson.setRoles(roles);

         Authentication authentication = mock(Authentication.class);
         when(authentication.getPrincipal()).thenReturn(currentPerson);

         SecurityContext securityContext = mock(SecurityContext.class);
         when(securityContext.getAuthentication()).thenReturn(authentication);
         SecurityContextHolder.setContext(securityContext);

         boolean result = personService.isCurrentPersonMentor();
         assertFalse(result);
     }
 */
    @Test
    void testAddEmailToCurrentUser() {
        PersonEntity currentPerson = new PersonEntity();
        currentPerson.setId(1L);

        when(personRepository.save(currentPerson)).thenReturn(currentPerson);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(currentPerson);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        String email = "test@example.com";
        PersonEntity result = personService.setEmailToMe(email);
        assertEquals(email, result.getEmail());
    }

    @Test
    void testLoadUserById_UserFound() {
        Long userId = 1L;
        PersonEntity personEntity = new PersonEntity();
        personEntity.setId(userId);

        when(personRepository.findById(userId)).thenReturn(Optional.of(personEntity));

        UserDetails userDetails = personService.loadUserById(userId);

        assertNotNull(userDetails);
        assertEquals(userId, ((PersonEntity) userDetails).getId());
    }

    @Test
    void testLoadUserById_UserNotFound() {
        Long userId = 1L;

        when(personRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> personService.loadUserById(userId));
    }

    @Test
    void testHasAnyRole_WithoutMatchingRoles() {
        Set<RoleEntity> userRoles = new HashSet<>();
        userRoles.add(new RoleEntity("ROLE_USER"));
        userRoles.add(new RoleEntity("ROLE_STUDENT"));

        PersonEntity user = new PersonEntity();
        user.setId(2L);
        user.setRoles(userRoles);

        when(authentication.getPrincipal()).thenReturn(user);

        boolean result = PersonService.hasAnyRole(List.of("ROLE_ADMIN", "ROLE_MENTOR"));

        assertFalse(result);
    }

    @Test
    void testHasAdvancedRole_WithoutAdvancedRole() {
        Set<RoleEntity> userRoles = new HashSet<>();
        userRoles.add(new RoleEntity(PersonService.ROLE_GUEST));

        PersonEntity user = new PersonEntity();
        user.setId(2L);
        user.setRoles(userRoles);

        when(authentication.getPrincipal()).thenReturn(user);

        boolean result = PersonService.hasAdvancedRole();

        assertFalse(result);
    }

    @Test
    void testSaveRole_WhenRoleExists() {

        String existingRoleName = "ROLE_USER";


        RoleEntity existingRole = new RoleEntity(existingRoleName);

        when(roleRepository.findByName(existingRoleName)).thenReturn(existingRole);

        RoleEntity savedRole = personService.saveRole(existingRoleName);

        assertEquals(existingRole, savedRole);
    }

    @Test
    void testGetRole_WhenRoleExists() {
        String existingRoleName = "ROLE_USER";
        RoleEntity existingRole = new RoleEntity(existingRoleName);

        when(roleRepository.findByName(existingRoleName)).thenReturn(existingRole);

        RoleEntity retrievedRole = personService.getRole(existingRoleName);
        assertEquals(existingRole, retrievedRole);
    }

    @Test
    void testGetRole_WhenRoleDoesNotExist() {
        String nonExistentRoleName = "ROLE_ADMIN";
        when(roleRepository.findByName(nonExistentRoleName)).thenReturn(null);
        RoleEntity retrievedRole = personService.getRole(nonExistentRoleName);
        assertNull(retrievedRole);
    }

    @Test
    void testGetRoles_WithExistingRoles() {
        String[] existingRoles = {"ROLE_USER", "ROLE_ADMIN"};

        RoleEntity roleUser = new RoleEntity(existingRoles[0]);
        RoleEntity roleAdmin = new RoleEntity(existingRoles[1]);

        when(roleRepository.findByName(existingRoles[0])).thenReturn(roleUser);
        when(roleRepository.findByName(existingRoles[1])).thenReturn(roleAdmin);

        Set<RoleEntity> roles = personService.getRoles(existingRoles);

        assertEquals(2, roles.size());
        assertTrue(roles.contains(roleUser));
        assertTrue(roles.contains(roleAdmin));
    }

    @Test
    void testGetRoles_WithNonExistentRoles() {
        String[] nonExistentRoles = {"ROLE_MODERATOR", "ROLE_GUEST"};
        when(roleRepository.findByName(nonExistentRoles[0])).thenReturn(null);
        when(roleRepository.findByName(nonExistentRoles[1])).thenReturn(null);

        Set<RoleEntity> roles = personService.getRoles(nonExistentRoles);
        assertEquals(0, roles.size());
    }
/*
    @Test
    void testAddRolesToPerson() {
        PersonEntity person = new PersonEntity();
        RoleEntity studentRole = new RoleEntity(PersonService.ROLE_STUDENT);
        RoleEntity adminRole = new RoleEntity(PersonService.ROLE_ADMIN);
        person.setRoles(Set.of(studentRole, adminRole));

        when(personRepository.save(any(PersonEntity.class))).thenReturn(person);

        PersonEntity savedPerson = personService.addRoles(person, Set.of(
                new RoleEntity("ROLE_GUEST"),
                new RoleEntity("ROLE_STUDENT"),
                new RoleEntity("ROLE_ADMIN")));
        assertTrue(savedPerson.getRoles().contains(studentRole));
        assertTrue(savedPerson.getRoles().contains(adminRole));
        assertFalse(savedPerson.getRoles().contains(new RoleEntity(PersonService.ROLE_GUEST)));
    }
*/
}
