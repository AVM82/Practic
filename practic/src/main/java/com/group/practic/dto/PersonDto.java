package com.group.practic.dto;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class PersonDto {

    private long id;

    private String name;

    private String discord;

    private String email;

    private String linkedin;

    private String profilePictureUrl;

    private LocalDateTime registered;
    
    private Set<String> roles;

    private Set<String> courses;


    PersonDto() {}


    public PersonDto(String name, String discord) {
        this.name = name;
        this.discord = discord;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        PersonDto that = (PersonDto) obj;
        return this == that || (Objects.equals(this.name, that.name)
                && Objects.equals(this.discord, that.discord));
    }


    @Override
    public int hashCode() {
        return Objects.hash(name, discord);
    }

}
