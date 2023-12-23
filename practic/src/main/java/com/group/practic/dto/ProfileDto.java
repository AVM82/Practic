package com.group.practic.dto;

import com.group.practic.entity.PersonEntity;
import com.group.practic.entity.ProfileEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileDto {
    private long profileId;
    private long personId;
    private String pictureUrl;
    private String name;
    private String surname;
    private String country;
    private String city;
    private String email;
    private String discord;
    private String personUrl;

    private boolean notificationEmail;
    private boolean notificationDiscord;

    public static ProfileDto map(ProfileEntity profileEntity, PersonEntity personEntity) {
        ProfileDto dto = new ProfileDto();
        dto.profileId = profileEntity.getId();
        dto.country = profileEntity.getCountry();
        dto.city = profileEntity.getCity();
        dto.notificationDiscord = profileEntity.isNotificationDiscord();
        dto.notificationEmail = profileEntity.isNotificationEmail();

        dto.personId = personEntity.getId();
        dto.pictureUrl = personEntity.getProfilePictureUrl();
        dto.email = personEntity.getEmail();
        dto.discord = personEntity.getDiscord();
        dto.personUrl = personEntity.getPersonPageUrl();

        String[] nameParts = personEntity.getName().split("\\s+");
        dto.name = nameParts[0];
        if (nameParts.length > 1) {
            dto.surname = nameParts[1];
        } else {
            dto.surname = nameParts[0];
        }

        return dto;
    }

}
