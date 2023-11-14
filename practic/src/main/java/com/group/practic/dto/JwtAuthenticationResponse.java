package com.group.practic.dto;

import lombok.Value;

@Value
public class JwtAuthenticationResponse {

    private String accessToken;

    private UserInfoDto user;

}
