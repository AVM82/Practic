package com.group.practic.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.Objects;


public class VerificationByEmailDto {

    @NotBlank
    @Min(value = 2)
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Min(value = 3)
    private String password;


    public VerificationByEmailDto(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }


    public VerificationByEmailDto() {}


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }


    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        VerificationByEmailDto that = (VerificationByEmailDto) o;
        return Objects.equals(name, that.name) && Objects.equals(email, that.email)
                && Objects.equals(password, that.password);
    }


    @Override
    public int hashCode() {
        return Objects.hash(name, email, password);
    }

}
