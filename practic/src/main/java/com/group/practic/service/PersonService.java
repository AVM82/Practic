package com.group.practic.service;

import com.group.practic.dto.PersonDto;
import com.group.practic.entity.PersonEntity;
import com.group.practic.entity.RoleEntity;
import com.group.practic.repository.PersonRepository;
import com.group.practic.util.Converter;
import jakarta.persistence.EntityExistsException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;



@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;


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
        return Optional.ofNullable(personRepository.save(Converter.convert(personDto)));
    }

    public PersonEntity getCurrentPerson() {
        OAuth2User authorization = getOauth2User();

        return personRepository.findByLinkedin(authorization.getAttribute("id")).orElse(null);
    }

    public PersonEntity createUserIfNotExists() {
        OAuth2User authorization = getOauth2User();

        Map<String, Object> authorizationAttributes = authorization.getAttributes();

        String linkedinId = authorizationAttributes.get("id").toString();

        if (personRepository.findByLinkedin(linkedinId).isPresent()) {
            return null;
        }

        PersonEntity personEntity = new PersonEntity(
                authorizationAttributes.get("localizedFirstName")
                        + " " + authorizationAttributes.get("localizedLastName"),
                        linkedinId);

        personEntity.addRole(new RoleEntity("USER"));

        return personRepository.save(personEntity);
    }

    private static OAuth2User getOauth2User() {
        return (OAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public Set<RoleEntity> findUserRolesById(long id) {
        PersonEntity foundPerson = personRepository.findById(id).orElse(null);

        if (foundPerson == null) {
            return Set.of();
        }

        return foundPerson.getRoles();
    }

    public PersonEntity addRoleToUserById(long id, String newRole) {
        PersonEntity foundPerson = personRepository.findById(id).orElse(null);

        if (foundPerson == null) {
            return null;
        }

        if (foundPerson.containsRole(newRole)) {
            throw new EntityExistsException("User with this role already exists " + newRole);
        }

        foundPerson.addRole(new RoleEntity(newRole));

        return personRepository.save(foundPerson);
    }

    public boolean isCurrentPersonMentor() {
        return getCurrentPerson().containsRole("MENTOR");
    }
}
