package com.group.practic.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.group.practic.dto.ProfileDto;
import com.group.practic.entity.PersonEntity;
import com.group.practic.entity.ProfileEntity;
import com.group.practic.repository.PersonRepository;
import com.group.practic.repository.ProfileRepository;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private ProfileService profileService;

    long profileId = 1L;
    long personId = 2L;
    String country = "Country";
    String city = "City";
    boolean notificationEmail = true;
    boolean notificationDiscord = false;
    String email = "test@example.com";
    String discord = "testDiscord";
    String personUrl = "http://example.com";
    String name = "John";
    String surname = "Doe";

    @Test
    void testUpdateProfile() {

        ProfileDto profileDto = getProfileDto();

        PersonEntity personEntity = new PersonEntity();
        personEntity.setId(personId);
        personEntity.setEmail(email);
        personEntity.setDiscord(discord);
        personEntity.setPersonPageUrl(personUrl);
        personEntity.setName(name + " " + surname);

        ProfileEntity profileEntity = new ProfileEntity();
        profileEntity.setId(profileId);
        profileEntity.setCountry(country);
        profileEntity.setCity(city);
        profileEntity.setNotificationEmail(notificationEmail);
        profileEntity.setNotificationDiscord(notificationDiscord);

        try (MockedStatic<PersonService> utilities = mockStatic(PersonService.class)) {
            utilities.when(PersonService::me).thenReturn(personEntity);


            when(profileRepository.findById(profileId)).thenReturn(Optional.of(profileEntity));
            when(personRepository.save(any())).thenReturn(personEntity);
            when(profileRepository.save(any())).thenReturn(profileEntity);

            Optional<ProfileDto> result = profileService.updateProfile(profileDto);
            assertEquals(profileDto, result.orElse(null));

            verify(profileRepository, times(1)).findById(profileId);
            verify(personRepository, times(1)).save(any());
            verify(profileRepository, times(1)).save(any());

        }
    }

    @NotNull
    private ProfileDto getProfileDto() {
        ProfileDto profileDto = new ProfileDto();
        profileDto.setProfileId(profileId);
        profileDto.setPersonId(personId);
        profileDto.setCountry(country);
        profileDto.setCity(city);
        profileDto.setNotificationEmail(notificationEmail);
        profileDto.setNotificationDiscord(notificationDiscord);
        profileDto.setEmail(email);
        profileDto.setDiscord(discord);
        profileDto.setPersonUrl(personUrl);
        profileDto.setName(name);
        profileDto.setSurname(surname);
        return profileDto;
    }

    @Test
    void testSetPersonEntity() {
        PersonEntity personEntity = new PersonEntity();

        ProfileService.setPersonEntity(getProfileDto(), personEntity);

        assertEquals(email, personEntity.getEmail());
        assertEquals(discord, personEntity.getDiscord());
        assertEquals(personUrl, personEntity.getPersonPageUrl());
        assertEquals(name + surname, personEntity.getName());
    }

    @Test
    void testSetProfileEntity() {
        ProfileEntity profileEntity = new ProfileEntity();

        ProfileService.setProfileEntity(getProfileDto(), profileEntity);

        assertEquals(country, profileEntity.getCountry());
        assertEquals(city, profileEntity.getCity());
        assertEquals(notificationEmail, profileEntity.isNotificationEmail());
        assertEquals(notificationDiscord, profileEntity.isNotificationDiscord());
    }
}