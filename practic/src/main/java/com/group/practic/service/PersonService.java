package com.group.practic.service;

import com.group.practic.dto.PersonDto;
import com.group.practic.entity.PersonEntity;
import com.group.practic.repository.PersonRepository;
import com.group.practic.util.Converter;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
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


  public Optional<PersonEntity> getByDiscord(String discord) {
    return personRepository.findByDiscord(discord);
  }


  public Optional<PersonEntity> getByLinkedin(String linkedin) {
    return personRepository.findByLinkedin(linkedin);
  }


  public List<PersonEntity> get(boolean inactive, boolean ban) {
    return personRepository.findAllByInactiveAndBan(inactive, ban);
  }


  public List<PersonEntity> get(String name, boolean inactive, boolean ban) {
    return personRepository.findAllByNameInactiveAndBan(name, inactive, ban);
  }


  public Optional<PersonEntity> create(PersonDto personDto) {
    return Optional.ofNullable(personRepository.save(Converter.convert(personDto)));
  }

}
