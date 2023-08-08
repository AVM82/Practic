package com.group.practic.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "mentor")
public class MentorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    int id;

    boolean inactive;

    @ManyToMany
    Set<CourseEntity> course;

    String pib;

    String notes;

    String email;

    String phone;

    long discordId;

    String discordName;

    String linkedinRef;


    public MentorEntity(int id, boolean inactive, String pib, String notes, String email,
                        String phone, long discordId, String discordName, String linkedInRef) {
        super();
        this.id = id;
        this.inactive = inactive;
        this.pib = pib;
        this.notes = notes;
        this.email = email;
        this.phone = phone;
        this.discordId = discordId;
        this.discordName = discordName;
        this.linkedinRef = linkedInRef;
    }


    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }


    public boolean isInactive() {
        return inactive;
    }


    public void setInactive(boolean inactive) {
        this.inactive = inactive;
    }


    public String getPib() {
        return pib;
    }


    public void setPib(String pib) {
        this.pib = pib;
    }


    public String getNotes() {
        return notes;
    }


    public void setNotes(String notes) {
        this.notes = notes;
    }


    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }


    public String getPhone() {
        return phone;
    }


    public void setPhone(String phone) {
        this.phone = phone;
    }


    public long getDiscordId() {
        return discordId;
    }


    public void setDiscordId(long discordId) {
        this.discordId = discordId;
    }


    public String getDiscordName() {
        return discordName;
    }


    public void setDiscordName(String discordName) {
        this.discordName = discordName;
    }


    public String getLinkedinRef() {
        return linkedinRef;
    }


    public void setLinkedinRef(String linkedinRef) {
        this.linkedinRef = linkedinRef;
    }

}
