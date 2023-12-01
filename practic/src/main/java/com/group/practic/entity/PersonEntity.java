package com.group.practic.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
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


@Table(name = "persons", uniqueConstraints = @UniqueConstraint(columnNames = {"email", "discord"}),
        indexes = @Index(columnList = "email"))
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class PersonEntity implements UserDetails {

    static final long serialVersionUID = 2865461614246570865L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    long id;

    boolean inactive;

    boolean ban;

    LocalDateTime registered = LocalDateTime.now();

    String email;

    @Column
    @NotBlank
    String name;

    String discord;

    @Column
    String linkedin;

    String contacts;

    String password;

    String profilePictureUrl;

    String personPageUrl;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "persons_roles", joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleEntity> roles = new HashSet<>();

    @OneToMany(mappedBy = "person", cascade = CascadeType.MERGE)
    private Set<StudentEntity> students = new HashSet<>();

    @OneToMany(mappedBy = "person", cascade = CascadeType.MERGE)
    private Set<MentorEntity> mentors = new HashSet<>();

    @OneToMany(mappedBy = "person", cascade = CascadeType.MERGE)
    private Set<ApplicantEntity> applicants = new HashSet<>();

    @OneToMany(mappedBy = "person", cascade = CascadeType.MERGE)
    private Set<GraduateEntity> garduates = new HashSet<>();


    public PersonEntity(String name, String linkedin, RoleEntity guestRole) {
        this.name = name;
        this.linkedin = linkedin;
        this.roles.add(guestRole);
    }


    public PersonEntity(String name, String password, String email, String linkedin,
            String profilePictureUrl, RoleEntity guestRole) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.linkedin = linkedin;
        this.profilePictureUrl = profilePictureUrl;
        this.roles.add(guestRole);
    }


    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles == null ? new HashSet<>()
                : roles.stream().map(p -> new SimpleGrantedAuthority("ROLE_" + p.getName()))
                        .collect(Collectors.toUnmodifiableSet());
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
        return !ban;
    }

}
