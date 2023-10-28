package com.group.practic.service;

import com.group.practic.dto.AuthUserDto;
import com.group.practic.dto.PersonDto;
import com.group.practic.dto.SignUpRequestDto;
import com.group.practic.entity.MentorEntity;
import com.group.practic.entity.PersonEntity;
import com.group.practic.entity.RoleEntity;
import com.group.practic.entity.StudentEntity;
import com.group.practic.exception.ResourceNotFoundException;
import com.group.practic.exception.UserAlreadyExistAuthenticationException;
import com.group.practic.repository.PersonRepository;
import com.group.practic.repository.RoleRepository;
import com.group.practic.security.user.LinkedinOauth2UserInfo;
import com.group.practic.security.user.Oauth2UserInfo;
import com.group.practic.util.Converter;
import jakarta.persistence.EntityExistsException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class PersonService implements UserDetailsService {

    public static final String ROLE_ADMIN = "ADMIN";

    public static final String ROLE_COLLABORATOR = "COLLABORATOR";

    public static final String ROLE_MENTOR = "MENTOR";

    public static final String ROLE_STUDENT = "STUDENT";

    public static final String ROLE_GUEST = "GUEST";

    public static final List<String> ROLES =
            List.of(ROLE_ADMIN, ROLE_COLLABORATOR, ROLE_MENTOR, ROLE_STUDENT, ROLE_GUEST);

    public static final List<String> ADVANCED_ROLES =
            List.of(ROLE_ADMIN, ROLE_COLLABORATOR, ROLE_MENTOR);

    PersonRepository personRepository;

    RoleRepository roleRepository;

    CourseService courseService;

    ApplicantService applicantService;

    RoleEntity roleGuest;


    @Autowired
    public PersonService(PersonRepository personRepository, RoleRepository roleRepository,
            CourseService courseService, ApplicantService applicantService) {
        this.courseService = courseService;
        this.applicantService = applicantService;
        this.personRepository = personRepository;
        this.roleRepository = roleRepository;
        ROLES.forEach(this::saveRole);
        this.roleGuest = getRole(ROLE_GUEST);
    }


    public static PersonEntity me() {
        return (PersonEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }


    public Optional<PersonDto> getMeDto() {
        return Optional.ofNullable(PersonDto.map(me()));
    }


    RoleEntity saveRole(String role) {
        RoleEntity roleExists = roleRepository.findByName(role);
        return roleExists != null ? roleExists : roleRepository.save(new RoleEntity(role));
    }


    public RoleEntity getRole(String role) {
        return roleRepository.findByName(role);
    }


    Set<RoleEntity> getRoles(String... roles) {
        Set<RoleEntity> roleSet = new HashSet<>();
        for (String role : roles) {
            RoleEntity roleEntity = getRole(role);
            if (roleEntity != null) {
                roleSet.add(roleEntity);
            }
        }
        return roleSet;
    }


    public List<PersonEntity> get() {
        return personRepository.findAll();
    }


    public Optional<PersonEntity> get(long id) {
        return personRepository.findById(id);
    }


    public List<PersonEntity> get(String name) {
        return personRepository.findAllByName(name);
    }


    public List<PersonEntity> get(boolean inactive, boolean ban) {
        return personRepository.findAllByInactiveAndBan(inactive, ban);
    }


    public List<PersonEntity> get(String name, boolean inactive, boolean ban) {
        return personRepository.findAllByNameAndInactiveAndBan(name, inactive, ban);
    }


    public Optional<PersonEntity> getByDiscord(String discord) {
        return personRepository.findByDiscord(discord);
    }


    public Optional<PersonEntity> getByLinkedin(String linkedin) {
        return personRepository.findByLinkedin(linkedin);
    }


    public Optional<PersonEntity> create(PersonDto personDto) {
        return Optional.of(personRepository.save(Converter.convert(personDto)));
    }

    public PersonEntity getPerson() {
        return me();
    }
    public Set<RoleEntity> findUserRolesById(long id) {
        Optional<PersonEntity> foundPerson = get(id);
        return foundPerson.isPresent() ? foundPerson.get().getRoles() : Set.of();
    }
    public Optional<PersonEntity> addRoleToUserById(long id, String newRole) {
        PersonEntity foundPerson = personRepository.findById(id).orElse(null);
        if (foundPerson != null) {
            if (foundPerson.getRoles().contains(getRole(newRole))) {
                throw new EntityExistsException("User with this role already exists " + newRole);
            }
            foundPerson.setRoles(Set.of(new RoleEntity(newRole)));
            return Optional.of(personRepository.save(foundPerson));
        }
        return Optional.empty();
    }
    public Optional<PersonEntity> addEmailToCurrentUser(String email) {
        Optional<PersonEntity> currentPerson = Optional.ofNullable(getPerson());
        if (currentPerson.isPresent()) {
            currentPerson.get().setContacts(email);
            return Optional.of(personRepository.save(currentPerson.get()));
        }
        return Optional.empty();
    }
    public PersonEntity loadUserByEmail(String email) {
        return personRepository.findByEmail(email).orElse(null);
    }
    public Optional<PersonEntity> getCurrentPerson() {
        return personRepository.findByLinkedin(getOauth2User().getAttribute("id"));
    }


    public PersonEntity createUserIfNotExists() {
        Map<String, Object> authorizationAttributes = getOauth2User().getAttributes();
        String linkedinId = authorizationAttributes.get("id").toString();
        return personRepository.findByLinkedin(linkedinId).isPresent() ? null
                : personRepository.save(new PersonEntity(
                        authorizationAttributes.get("localizedFirstName") + " "
                                + authorizationAttributes.get("localizedLastName"),
                        linkedinId, Set.of(roleGuest)));
    }


    public OAuth2User getOauth2User() {
        return (OAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }


    public PersonEntity addRolesToPerson(PersonEntity person, Set<RoleEntity> newRoles) {
        Set<RoleEntity> personRoles = person.getRoles();
        personRoles.addAll(newRoles);
        if (personRoles.size() > 1) {
            personRoles.remove(roleGuest);
        }
        return personRepository.saveAndFlush(person);
    }


    public Optional<PersonEntity> setEmailToMe(String email) {
        PersonEntity me = me();
        if (me != null) {
            me.setContacts(email);
            me = personRepository.save(me);
        }
        return Optional.ofNullable(me);
    }


    public Optional<PersonEntity> findByEmail(String email) {
        return personRepository.findByEmail(email);
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return personRepository.findByEmail(email).orElse(createUserIfNotExists());
    }


    @Transactional
    public AuthUserDto processUserRegistration(Map<String, Object> attributes, OidcIdToken idToken,
            OidcUserInfo userInfo) {
        Oauth2UserInfo oauth2UserInfo = new LinkedinOauth2UserInfo(attributes);
        Optional<PersonEntity> user = personRepository.findByEmail(oauth2UserInfo.getEmail());
        PersonEntity person =
                user.map(personEntity -> updateExistingUser(personEntity, oauth2UserInfo))
                        .orElseGet(() -> registerNewUser(toUserRegistrationObject(oauth2UserInfo)));
        return AuthUserDto.create(person, attributes, idToken, userInfo);
    }


    public PersonEntity updateExistingUser(PersonEntity existingUser,
            Oauth2UserInfo oauth2UserInfo) {
        boolean differences = false;
        if (differences = !oauth2UserInfo.getName().equals(existingUser.getName()))
            existingUser.setName(oauth2UserInfo.getName());
        if (differences = differences || !existingUser.getLinkedin().equals(oauth2UserInfo.getId()))
            existingUser.setLinkedin(oauth2UserInfo.getId());
        if (differences = differences || !existingUser.getProfilePictureUrl().equals(oauth2UserInfo.getImageUrl()) )
            existingUser.setProfilePictureUrl(oauth2UserInfo.getImageUrl());
        return differences ? personRepository.save(existingUser) : existingUser;
    }


    private SignUpRequestDto toUserRegistrationObject(Oauth2UserInfo oauth2UserInfo) {
        return SignUpRequestDto.builder().providerUserId(oauth2UserInfo.getId())
                .name(oauth2UserInfo.getName()).email(oauth2UserInfo.getEmail())
                .profilePictureUrl(oauth2UserInfo.getImageUrl()).password("changeit").build();
    }


    @Transactional
    public UserDetails loadUserById(Long id) {
        return personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }


    @Transactional
    public PersonEntity registerNewUser(final SignUpRequestDto userDetails) {
        Optional<PersonEntity> personEntity = personRepository.findByEmail(userDetails.getEmail());
        if (personEntity.isPresent()) {
            throw new UserAlreadyExistAuthenticationException(
                    "User with email " + userDetails.getEmail() + " already exist");
        }
        PersonEntity newPerson = new PersonEntity();
        newPerson.setName(userDetails.getName());
        newPerson.setPassword(userDetails.getPassword());
        newPerson.setEmail(userDetails.getEmail());
        newPerson.setLinkedin(userDetails.getProviderUserId());
        newPerson.setProfilePictureUrl(userDetails.getProfilePictureUrl());
        return addRolesToPerson(personRepository.save(newPerson), Set.of(roleGuest));
    }


    public boolean hasAnyRole(List<String> roles) {
        PersonEntity me = me();
        return me != null && me.getRoles().stream()
                .anyMatch(roleEntity -> roles.contains(roleEntity.getName()));
    }


    public boolean hasAdvancedRole() {
        return hasAnyRole(ADVANCED_ROLES);
    }


    public PersonEntity addStudent(StudentEntity student) {
        PersonEntity person = student.getPerson();
        person.getStudents().add(student);
        return this.addRolesToPerson(person, Set.of(getRole(ROLE_STUDENT)));
    }


    public PersonEntity addMentor(MentorEntity mentor) {
        PersonEntity person = mentor.getPerson();
        person.getMentors().add(mentor);
        return this.addRolesToPerson(person, Set.of(getRole(ROLE_MENTOR)));
    }


}
