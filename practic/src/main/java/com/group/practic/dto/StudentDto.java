package com.group.practic.dto;

import java.util.Objects;

public final class StudentDto {
    private final String pib;
    private final String notes;
    private final String email;
    private final String phone;
    private final Long discordId;

    StudentDto(String pib, String notes, String email, String phone, Long discordId) {
        this.pib = pib;
        this.notes = notes;
        this.email = email;
        this.phone = phone;
        this.discordId = discordId;
    }

    public String pib() {
        return pib;
    }

    public String notes() {
        return notes;
    }

    public String email() {
        return email;
    }

    public String phone() {
        return phone;
    }

    public Long discordId() {
        return discordId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        var that = (StudentDto) obj;
        return Objects.equals(this.pib, that.pib)
                && Objects.equals(this.notes, that.notes)
                && Objects.equals(this.email, that.email)
                && Objects.equals(this.phone, that.phone)
                && Objects.equals(this.discordId, that.discordId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pib, notes, email, phone, discordId);
    }

    @Override
    public String toString() {
        return "StudentDTO["
                + "pib=" + pib + ", "
                + "notes=" + notes + ", "
                + "email=" + email + ", "
                + "phone=" + phone + ", "
                + "discordId=" + discordId + ']';
    }

}
