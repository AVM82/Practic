package com.group.practic.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.Collection;

@Table(name = "person",
        uniqueConstraints = @UniqueConstraint(columnNames = "name"))
@Entity

public class PersonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    @NotBlank
    private String name;
    @Column
    @NotBlank
    private String linkedin;

    @ManyToMany(fetch = FetchType.EAGER,
            cascade = CascadeType.ALL)
    @JoinTable(name = "persons_roles",joinColumns =
    @JoinColumn(name = "person_id",referencedColumnName = "id"),inverseJoinColumns =
    @JoinColumn(name = "role_id",referencedColumnName = "id"))
    private Collection<RoleEntity> roles;

    public PersonEntity(String name, String linkedin, Collection<RoleEntity> roles) {
        this.name = name;
        this.linkedin = linkedin;
        this.roles = roles;
    }

    public PersonEntity() {
    }

    public String getName() {
        return name;
    }

    public PersonEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public PersonEntity setLinkedin(String linkedin) {
        this.linkedin = linkedin;
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Collection<RoleEntity> getRoles() {
        return roles;
    }

    public PersonEntity setRoles(Collection<RoleEntity> roles) {
        this.roles = roles;
        return this;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", linkedin='" + linkedin + '\'' +
                ", roles=" + roles +
                '}';
    }
}