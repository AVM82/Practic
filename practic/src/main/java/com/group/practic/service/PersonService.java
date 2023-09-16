package com.group.practic.service;

import com.group.practic.dto.AuthUserDto;
import com.group.practic.dto.PersonDto;
import com.group.practic.dto.SignUpRequestDto;
import com.group.practic.entity.PersonEntity;
import com.group.practic.entity.RoleEntity;
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
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
@AllArgsConstructor
public class PersonService implements UserDetailsService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private RoleRepository roleRepository;


    public List<PersonEntity> get() {
        return personRepository.findAll();
    }


    public Optional<PersonEntity> get(long id) {
        return personRepository.findById(id);
    }


    public Optional<PersonEntity> get(String name) {
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
                linkedinId,
                Set.of(new RoleEntity("USER"))));
    }


    private static OAuth2User getOauth2User() {
        return (OAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }


    public Set<RoleEntity> findUserRolesById(long id) {
        Optional<PersonEntity> foundPerson = get(id);
        return foundPerson.isPresent() ? foundPerson.get().getRoles() : Set.of();
    }


    public Optional<PersonEntity> addRoleToUserById(long id, String newRole) {
        PersonEntity foundPerson = personRepository.findById(id).orElse(null);
        if (foundPerson != null) {
            if (foundPerson.containsRole(newRole)) {
                throw new EntityExistsException("User with this role already exists " + newRole);
            }
            foundPerson.setRoles(Set.of(new RoleEntity(newRole)));
            return Optional.of(personRepository.save(foundPerson));
        }
        return Optional.empty();
    }


    public boolean isCurrentPersonMentor() {
        Optional<PersonEntity> currentPerson = getCurrentPerson();
        return currentPerson.isPresent() && currentPerson.get().containsRole("MENTOR");
    }


    public Optional<PersonEntity> addEmailToCurrentUser(String email) {
        Optional<PersonEntity> currentPerson = getCurrentPerson();
        if (currentPerson.isPresent()) {
            currentPerson.get().setContacts(email);
            return Optional.of(personRepository.save(currentPerson.get()));
        }
        return Optional.empty();
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return personRepository.findPersonEntityByEmail(email).orElse(createUserIfNotExists());
    }


    @Transactional
    public AuthUserDto processUserRegistration(Map<String, Object> attributes, OidcIdToken idToken,
                                               OidcUserInfo userInfo) {
        Oauth2UserInfo oauth2UserInfo = new LinkedinOauth2UserInfo(attributes);
        Optional<PersonEntity> user = personRepository
                .findPersonEntityByEmail(oauth2UserInfo.getEmail());
        PersonEntity person =
                user.map(personEntity -> updateExistingUser(personEntity, oauth2UserInfo))
                        .orElseGet(() -> registerNewUser(toUserRegistrationObject(oauth2UserInfo)));
        return AuthUserDto.create(person, attributes, idToken, userInfo);
    }


    private PersonEntity updateExistingUser(PersonEntity existingUser,
                                            Oauth2UserInfo oauth2UserInfo) {
        existingUser.setName(oauth2UserInfo.getName());
        existingUser.setLinkedin(oauth2UserInfo.getId());
        existingUser.setProfilePictureUrl(oauth2UserInfo.getImageUrl());
        return personRepository.save(existingUser);
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
        Optional<PersonEntity> personEntity = personRepository
                .findPersonEntityByEmail(userDetails.getEmail());
        if (personEntity.isPresent()) {
            throw new UserAlreadyExistAuthenticationException(
                    "User with email " + userDetails.getEmail() + " already exist");
        }
        final HashSet<RoleEntity> roles = new HashSet<>();
        roles.add(roleRepository.findByName("USER"));
        PersonEntity newPerson = new PersonEntity();
        newPerson.setInactive(false);
        newPerson.setName(userDetails.getName());
        newPerson.setPassword(userDetails.getPassword());
        newPerson.setEmail(userDetails.getEmail());
        newPerson.setLinkedin(userDetails.getProviderUserId());
        newPerson.setProfilePictureUrl(userDetails.getProfilePictureUrl());
        newPerson.setRoles(roles);
        newPerson.setCourses(new HashSet<>());
        newPerson = personRepository.save(newPerson);
        personRepository.flush();
        return newPerson;
    }

    public PersonEntity save(PersonEntity person) {
        return personRepository.save(person);
    }

}
