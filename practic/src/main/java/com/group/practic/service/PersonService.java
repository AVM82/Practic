package com.group.practic.service;

import com.group.practic.dto.ApplicantDto;
import com.group.practic.dto.AuthUserDto;
import com.group.practic.dto.SignUpRequestDto;
import com.group.practic.entity.CourseEntity;
import com.group.practic.entity.MentorEntity;
import com.group.practic.entity.PersonEntity;
import com.group.practic.entity.RoleEntity;
import com.group.practic.entity.StudentEntity;
import com.group.practic.exception.ResourceNotFoundException;
import com.group.practic.repository.PersonRepository;
import com.group.practic.repository.RoleRepository;
import com.group.practic.security.user.LinkedinOauth2UserInfo;
import com.group.practic.security.user.Oauth2UserInfo;
import com.group.practic.util.PasswordGenerator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class PersonService implements UserDetailsService {

    public static final String ROLE_ADMIN = "ADMIN";

    public static final String ROLE_COLLABORATOR = "COLLABORATOR";

    public static final String ROLE_COMRADE = "COMRADE";

    public static final String ROLE_MENTOR = "MENTOR";

    public static final String ROLE_STUDENT = "STUDENT";

    public static final String ROLE_GUEST = "GUEST";

    public static final List<String> ROLES = List.of(ROLE_ADMIN, ROLE_COLLABORATOR, ROLE_COMRADE,
            ROLE_MENTOR, ROLE_STUDENT, ROLE_GUEST);

    public static final List<String> ADVANCED_ROLES =
            List.of(ROLE_ADMIN, ROLE_COLLABORATOR, ROLE_COMRADE);

    PersonRepository personRepository;

    RoleRepository roleRepository;

    CourseService courseService;

    ApplicantService applicantService;

    EmailSenderService emailSenderService;
    
    RoleEntity roleGuest;

    RoleEntity roleStudent;

    RoleEntity roleMentor;

    RoleEntity roleComrade;

    RoleEntity roleCollaborator;

    RoleEntity roleAdmin;

    PasswordEncoder passwordEncoder;
    
    @Value("${email.password.message}")
    private String emailMessage;
    
    @Value("${email.password.header}")
    private String emailHeader;

    
    @Autowired
    public PersonService(PersonRepository personRepository, RoleRepository roleRepository,
            ApplicantService applicantService, CourseService courseService,
            EmailSenderService emailSenderService, PasswordEncoder passwordEncoder) {
        this.courseService = courseService;
        this.applicantService = applicantService;
        this.emailSenderService = emailSenderService;
        this.personRepository = personRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        ROLES.forEach(this::saveRole);
        this.roleGuest = getRole(ROLE_GUEST);
        this.roleStudent = getRole(ROLE_STUDENT);
        this.roleMentor = getRole(ROLE_MENTOR);
        this.roleComrade = getRole(ROLE_COMRADE);
        this.roleCollaborator = getRole(ROLE_COLLABORATOR);
        this.roleAdmin = getRole(ROLE_ADMIN);
    }


    public static PersonEntity me() {
        return (PersonEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }


    RoleEntity saveRole(String role) {
        RoleEntity roleExists = roleRepository.findByName(role);
        return roleExists != null ? roleExists : roleRepository.save(new RoleEntity(role));
    }


    public RoleEntity getRole(String role) {
        return roleRepository.findByName(role);
    }


    Set<RoleEntity> getRoles(String... roles) {
        return List.of(roles).stream().map(this::getRole).filter(Objects::nonNull)
                .collect(Collectors.toSet());
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


    public Optional<PersonEntity> getByEmail(String email) {
        return personRepository.findByEmail(email);
    }


    public Optional<PersonEntity> getByLinkedin(String linkedin) {
        return personRepository.findByLinkedin(linkedin);
    }


    protected void excludeGuestRole(Set<RoleEntity> roles) {
        if (roles.size() > 1) {
            roles.remove(roleGuest);
        }
    }


    protected void includeGuestRole(Set<RoleEntity> roles) {
        if (roles.isEmpty()) {
            roles.add(roleGuest);
        }
    }


    public PersonEntity addRole(PersonEntity person, String roleName) {
        return addRole(person, getRole(roleName));
    }


    public PersonEntity addRole(PersonEntity person, RoleEntity role) {
        Set<RoleEntity> personRoles = person.getRoles();
        personRoles.add(role);
        excludeGuestRole(personRoles);
        return personRepository.save(person);
    }


    public PersonEntity addRoles(PersonEntity person, Set<RoleEntity> newRoles) {
        Set<RoleEntity> personRoles = person.getRoles();
        personRoles.addAll(newRoles);
        excludeGuestRole(personRoles);
        return personRepository.save(person);
    }


    public PersonEntity removeRole(PersonEntity person, String roleName) {
        return addRole(person, getRole(roleName));
    }


    public PersonEntity removeRole(PersonEntity person, RoleEntity role) {
        Set<RoleEntity> personRoles = person.getRoles();
        if (personRoles.remove(role)) {
            includeGuestRole(personRoles);
            personRepository.save(person);
        }
        return person;
    }


    public PersonEntity removeRoles(PersonEntity person, Set<RoleEntity> roles) {
        Set<RoleEntity> personRoles = person.getRoles();
        if (personRoles.removeAll(roles)) {
            includeGuestRole(personRoles);
            personRepository.save(person);
        }
        return person;
    }


    public PersonEntity setEmailToMe(String email) {
        PersonEntity me = me();
        me.setContacts(email);
        return personRepository.save(me);
    }


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return personRepository.findByEmail(email).orElse(createUserIfNotExists());
    }


    public PersonEntity createUserIfNotExists() {
        Map<String, Object> authorizationAttributes =
                ((OAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                        .getAttributes();
        String linkedinId = authorizationAttributes.get("id").toString();
        return personRepository.findByLinkedin(linkedinId).isPresent() ? null
                : personRepository.save(new PersonEntity(
                        authorizationAttributes.get("localizedFirstName") + " "
                                + authorizationAttributes.get("localizedLastName"),
                        linkedinId, roleGuest));
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
        if (!oauth2UserInfo.getName().equals(existingUser.getName())
                || !existingUser.getLinkedin().equals(oauth2UserInfo.getId())
                || !existingUser.getProfilePictureUrl().equals(oauth2UserInfo.getImageUrl())) {
            existingUser.setName(oauth2UserInfo.getName());
            existingUser.setLinkedin(oauth2UserInfo.getId());
            existingUser.setProfilePictureUrl(oauth2UserInfo.getImageUrl());
            return personRepository.save(existingUser);
        }
        return existingUser;
    }


    private SignUpRequestDto toUserRegistrationObject(Oauth2UserInfo oauth2UserInfo) {
        String name = oauth2UserInfo.getName();
        String email = oauth2UserInfo.getEmail();
        String password = PasswordGenerator.generateRandomPassword();
        String message = String.format(emailMessage, name, password);
        emailSenderService.sendEmail(email, emailHeader, message);
        return SignUpRequestDto.builder().providerUserId(oauth2UserInfo.getId())
                .name(name).email(email)
                .profilePictureUrl(oauth2UserInfo.getImageUrl())
                .password(passwordEncoder.encode(password)).build();
    }

    @Transactional
    public UserDetails loadUserById(long id) {
        return get(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }


    @Transactional
    public PersonEntity registerNewUser(final SignUpRequestDto userDetails) {
        return personRepository.save(new PersonEntity(userDetails.getName(),
                userDetails.getPassword(), 
                userDetails.getEmail(),
                userDetails.getProviderUserId(),
                userDetails.getProfilePictureUrl(),
                roleGuest));
    }

    
    public PersonEntity save(PersonEntity person) {
        return personRepository.save(person);
    }
    

    public static boolean hasAnyRole(List<String> roles) {
        return me().getRoles().stream()
                .anyMatch(roleEntity -> roles.contains(roleEntity.getName()));
    }


    public static boolean hasAdvancedRole() {
        return hasAnyRole(ADVANCED_ROLES);
    }


    public Optional<ApplicantDto> createApplication(CourseEntity course) {
        return applicantService.create(me(), course).map(ApplicantDto::map);
    }


    public PersonEntity addMentor(MentorEntity mentor) {
        PersonEntity person = mentor.getPerson();
        person.getMentors().add(mentor);
        return person.getMentors().size() == 1 ? addRole(person, roleMentor)
                : personRepository.save(person);
    }


    public PersonEntity removeMentor(MentorEntity mentor) {
        PersonEntity person = mentor.getPerson();
        person.getMentors().remove(mentor);
        return person.getMentors().isEmpty() ? removeRole(person, roleMentor)
                : personRepository.save(person);
    }


    public PersonEntity addStudent(StudentEntity student) {
        PersonEntity person = student.getPerson();
        person.getStudents().add(student);
        return person.getStudents().size() == 1 ? addRole(person, roleStudent)
                : personRepository.save(person);
    }


    public PersonEntity removeStudent(StudentEntity student) {
        PersonEntity person = student.getPerson();
        person.getStudents().remove(student);
        return person.getStudents().isEmpty() ? removeRole(person, roleStudent)
                : personRepository.save(person);
    }

}
