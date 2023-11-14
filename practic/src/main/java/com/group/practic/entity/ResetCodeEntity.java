package com.group.practic.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.Getter;

@Getter
@Entity
@Table(name = "reset_code")
public class ResetCodeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @OneToOne
    private PersonEntity person;

    @NotBlank
    private String code;
    @NotBlank
    private LocalDateTime expiredAt;

    public ResetCodeEntity() {
    }

    public ResetCodeEntity(PersonEntity person, String code, LocalDateTime expiredAt) {
        this.person = person;
        this.code = code;
        this.expiredAt = expiredAt;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setPerson(PersonEntity person) {
        this.person = person;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setExpiredAt(LocalDateTime expiredAt) {
        this.expiredAt = expiredAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ResetCodeEntity that = (ResetCodeEntity) o;
        return id == that.id && Objects.equals(person, that.person)
                && Objects.equals(code, that.code)
                && Objects.equals(expiredAt, that.expiredAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, person, code, expiredAt);
    }
}
