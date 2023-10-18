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
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


@Table(name = "person", uniqueConstraints = @UniqueConstraint(columnNames = {"email", "discord"}))
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class PersonEntity implements UserDetails {

    private static final long serialVersionUID = 2865461614246570865L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    boolean inactive;

    boolean ban;
    
    LocalDateTime registered;

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

    private String password;

    private String profilePictureUrl;


    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "persons_roles", joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleEntity> roles;


    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "person_application", joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id"))
    private Set<CourseEntity> courses;


    public PersonEntity(String name, String linkedin) {
        this.name = name;
        this.linkedin = linkedin;
        this.registered = LocalDateTime.now();
    }


    public PersonEntity(String name, String linkedin, Set<RoleEntity> roles) {
        this.name = name;
        this.linkedin = linkedin;
        this.roles = roles;
        this.registered = LocalDateTime.now();
    }




    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<SimpleGrantedAuthority> authorities = new HashSet<>();
        if (roles != null) {
            authorities = roles.stream().map(p -> new SimpleGrantedAuthority("ROLE_" + p.getName()))
                    .collect(Collectors.toUnmodifiableSet());
        }
        return authorities;
    }


    @JsonIgnore
    @Override
    public String getPassword() {
        return null;
    }


    @JsonIgnore
    @Override
    public String getUsername() {
        return name;
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
        return roles.stream().anyMatch(personRole -> personRole.getName().equals(role));
    }

}
