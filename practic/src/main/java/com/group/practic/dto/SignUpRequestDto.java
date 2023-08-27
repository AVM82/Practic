package com.group.practic.dto;

import jakarta.validation.constraints.NotEmpty;
import java.net.URL;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SignUpRequestDto {

    private Long userId;

    private String providerUserId;

    @NotEmpty
    private String name;

    @NotEmpty
    private String email;

    private String profilePictureUrl;

    private String password;

}
