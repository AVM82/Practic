package com.group.practic.dto;

import com.group.practic.entity.FeedbackEntity;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeedbackDto {

    private long id;

    private long personId;

    private String name;

    private String profilePictureUrl;

    private String date;

    private String feedback;

    private int likes;

    private Set<Long> likedByPerson;


    public static FeedbackDto map(FeedbackEntity entity) {
        FeedbackDto dto = new FeedbackDto();
        dto.id = entity.getId();
        dto.likes = entity.getLikes();
        dto.name = entity.getName();
        dto.profilePictureUrl = entity.getProfilePictureUrl();
        dto.feedback = entity.getFeedback();
        dto.date = entity.getDateTime().toString();
        dto.personId = entity.getPersonId();
        dto.likedByPerson = entity.getLikedByPerson();
        return dto;
    }

}
