package com.group.practic.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import java.util.HashSet;
import java.util.Set;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;



@Table(name = "person", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "discord"}))
@Entity
public class PersonEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    boolean inactive;

    boolean ban;

    @Column(unique = true)
    private String email;

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
    private Set<RoleEntity> roles = new HashSet<>();


    public PersonEntity() {
    }

    public PersonEntity(String name, String linkedin) {
        this.name = name;
        this.linkedin = linkedin;
    }

    public PersonEntity(String name, String linkedin, String contacts) {
        this.name = name;
        this.linkedin = linkedin;
        this.contacts = contacts;
    }

    public PersonEntity(String name, String discord, String linkedin, String contacts,
                        Set<RoleEntity> roles) {
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


    public Set<RoleEntity> getRoles() {
        return roles;
    }


    public void setRoles(Set<RoleEntity> roles) {
        this.roles = roles;
    }


    public void addRole(RoleEntity role) {
        this.roles.add(role);
    }


    public void removeRole(RoleEntity role) {
        this.roles.remove(role);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    @Override
    public String toString() {
        return "Person{" + "id=" + id + ", name='" + name + '\'' + ", linkedin='" + linkedin + '\''
                + ", roles=" + roles + '}';
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return null;
    }

    @JsonIgnore
    @Override
    public String getUsername() {
        return getName();
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }

    public boolean containsRole(String role) {
        return roles.stream()
                .anyMatch(personRole ->
                        personRole.getName().equals(role));
    }
}
