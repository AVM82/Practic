package com.group.practic.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "pre_verification_user")
public class PreVerificationUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @NotBlank
    @Size(min = 2)
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 4)
    private String password;

    @NotBlank
    private String token;

    @NotNull
    private LocalDateTime expiredAt;

    public PreVerificationUserEntity() {
    }

    public PreVerificationUserEntity(String name,
                                     String email, String password,
                                     String token, LocalDateTime expiredAt) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.token = token;
        this.expiredAt = expiredAt;
    }
}