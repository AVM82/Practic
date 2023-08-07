package com.group.practic.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "student")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class StudentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;

    String pib;

    String notes;

    String email;

    String phone;

    Long discordId;
    String discordName;

    String linkedInRef;

    String githubRef;

    String skypeRef;

    String facebookRef;

    String telegramRef;

    String instagramRef;

    String locality;

    public StudentEntity(String pib, String notes, String email, String phone, Long discordId) {
        this.pib = pib;
        this.notes = notes;
        this.email = email;
        this.phone = phone;
        this.discordId = discordId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
