package com.group.practic.dto;

import lombok.Getter;

@Getter
public class JwtAuthenticationResponse {

    private String accessToken;

    private PersonDto user;

    
    public JwtAuthenticationResponse(String accessToken, PersonDto user) {
        this.accessToken = accessToken;
        this.user = user;
    }

}
