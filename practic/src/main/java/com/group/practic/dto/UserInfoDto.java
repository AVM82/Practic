package com.group.practic.dto;

import java.util.List;
import lombok.Value;

@Value
public class UserInfoDto {
    private String id;
    private String name;
    private String  email;
    private List<String> roles;
}
