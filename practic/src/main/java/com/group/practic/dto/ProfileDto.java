package com.group.practic.dto;

import com.group.practic.entity.PersonEntity;
import com.group.practic.entity.ProfileEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileDto {
    private long id;
    private String name;
    private String surname;
    private String profileCountry;
    private String profileCity;
    private String profileEmail;
    private String profileDiscord;
    private String profileLinkidIn;


    private boolean isNotificationEmail;
    private boolean isNotificationDiscord;

    public ProfileDto map(ProfileEntity profileEntity, PersonEntity personEntity) {
        ProfileDto dto = new ProfileDto();
        dto.id = profileEntity.getId();
        String[] nameParts = personEntity.getName().split("\\s+");
        dto.name = nameParts[0];
        if (nameParts.length > 1) {
            dto.surname = nameParts[1];
        } else {
            dto.surname = nameParts[0];
        }

        dto.profileCountry = profileEntity.getProfileCountry();
        dto.profileCity = profileEntity.getProfileCity();
        dto.profileEmail = personEntity.getEmail();
        dto.profileDiscord = profileEntity.getProfileDiscord();
        dto.profileLinkidIn = profileEntity.getProfileLinkidIn();
        dto.isNotificationDiscord = profileEntity.isNotificationDiscord();
        dto.isNotificationEmail = profileEntity.isNotificationEmail();


        return dto;
    }

}
