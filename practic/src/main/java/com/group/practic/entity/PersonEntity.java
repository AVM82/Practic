package com.group.practic.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import java.util.Collection;


@Table(name = "person", uniqueConstraints = @UniqueConstraint(columnNames = { "name", "discord" }))
@Entity
public class PersonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    boolean inactive;

    boolean ban;

    @Column
    @NotBlank
    private String name;

    @NotBlank
    private String discord;

    @Column
    @NotBlank
    private String linkedin;

    private String contacts;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "persons_roles",
            joinColumns = @JoinColumn(name = "person_id", referencedColumnName = "id"), 
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Collection<RoleEntity> roles;


    public PersonEntity() {
    }


    public PersonEntity(String name, String discord, String linkedin, String contacts,
            Collection<RoleEntity> roles) {
        this.name = name;
        this.discord = discord;
        this.linkedin = linkedin;
        this.contacts = contacts;
        this.roles = roles;
    }


    public long getId() {
        return id;
    }


    public void setId(long id) {
        this.id = id;
    }


    public boolean isInactive() {
        return inactive;
    }


    public void setInactive(boolean inactive) {
        this.inactive = inactive;
    }


    public boolean isBan() {
        return ban;
    }


    public void setBan(boolean ban) {
        this.ban = ban;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getDiscord() {
        return discord;
    }


    public void setDiscord(String discord) {
        this.discord = discord;
    }


    public String getLinkedin() {
        return linkedin;
    }


    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }


    public String getContacts() {
        return contacts;
    }


    public void setContacts(String contacts) {
        this.contacts = contacts;
    }


    public Collection<RoleEntity> getRoles() {
        return roles;
    }


    public void setRoles(Collection<RoleEntity> roles) {
        this.roles = roles;
    }


    public void setRole(RoleEntity role) {
        this.roles.add(role);
    }


    public void removeRole(RoleEntity role) {
        this.roles.remove(role);
    }


    @Override
    public String toString() {
        return "Person{" + "id=" + id + ", name='" + name + '\'' + ", linkedin='" + linkedin + '\''
                + ", roles=" + roles + '}';
    }

}
