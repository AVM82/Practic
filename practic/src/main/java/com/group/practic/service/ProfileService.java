package com.group.practic.service;

import com.group.practic.dto.ProfileDto;
import com.group.practic.entity.PersonEntity;
import com.group.practic.entity.ProfileEntity;
import com.group.practic.repository.PersonRepository;
import com.group.practic.repository.ProfileRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    ProfileRepository profileRepository;
    PersonRepository personRepository;

    @Autowired
    public ProfileService(ProfileRepository profileRepository,
                          PersonRepository personRepository) {
        this.profileRepository = profileRepository;
        this.personRepository = personRepository;
    }

    public Optional<ProfileEntity> get(long id) {
        return profileRepository.findById(id);
    }

    public Optional<ProfileEntity> get(PersonEntity person) {
        return profileRepository.findByPerson(person);
    }


    public ProfileDto getProfile(PersonEntity person) {
        Optional<ProfileEntity> profileExists = get(person);
        return ProfileDto.map(profileExists.isPresent()
                ? profileExists.get()
                : profileRepository.save(new ProfileEntity(person)), person);

    }

    public Optional<ProfileDto> updateProfile(ProfileDto profileDto) {
        Optional<ProfileEntity> profile = get(profileDto.getProfileId());
        PersonEntity person = PersonService.me();

        if (profile.isEmpty() || (profileDto.getPersonId() != person.getId())) {
            return Optional.empty();
        }

        ProfileEntity profileEntity = profile.get();

        setProfileEntity(profileDto, profileEntity);
        setPersonEntity(profileDto, person);
        profileRepository.save(profileEntity);
        personRepository.save(person);
        return Optional.of(profileDto);
    }

    public static void setPersonEntity(ProfileDto profileDto, PersonEntity person) {
        String space = " ";
        person.setEmail(profileDto.getEmail());
        person.setDiscord(profileDto.getDiscord());
        person.setPersonPageUrl(profileDto.getPersonUrl());
        person.setName(profileDto.getName() + space + profileDto.getSurname());
    }

    public static void setProfileEntity(ProfileDto profileDto, ProfileEntity profileEntity) {
        profileEntity.setCountry(profileDto.getCountry());
        profileEntity.setCity(profileDto.getCity());
        profileEntity.setNotificationDiscord(profileDto.isNotificationDiscord());
        profileEntity.setNotificationEmail(profileDto.isNotificationEmail());
    }
}
