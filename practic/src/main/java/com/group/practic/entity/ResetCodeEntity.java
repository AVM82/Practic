package com.group.practic.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "reset_code")
public class ResetCodeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    private String email;

    private String code;

    private LocalDateTime expiredAt;


    public ResetCodeEntity() {}


    public ResetCodeEntity(long id, String email, String code, long expiringMinutes) {
        this.id = id;
        this.email = email;
        this.code = code;
        this.expiredAt = LocalDateTime.now().plusMinutes(expiringMinutes);
    }

}
