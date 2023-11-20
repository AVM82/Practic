package com.group.practic.dto;


import com.group.practic.entity.FeedbackEntity;
import java.util.Set;
import lombok.Getter;

@Getter
public class FeedbackDto {

    private long id;

    private long personId;

    private String name;

    private  String profilePictureUrl;

    private String date;

    private String feedback;

    private int likes;

    private Set<Long> likedByPerson;


    public static FeedbackDto map(FeedbackEntity entity) {
        FeedbackDto dto = new FeedbackDto();
        dto.id = entity.getId();
        dto.likes = entity.getLikes();
        dto.name = entity.getPerson().getName();
        dto.profilePictureUrl = entity.getPerson().getProfilePictureUrl();
        dto.feedback = entity.getFeedback();
        dto.date = entity.getDateTime().toString();
        dto.personId = entity.getPerson().getId();
        dto.likedByPerson = entity.getLikedByPerson();
        return dto;
    }

}
