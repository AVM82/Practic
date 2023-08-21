package com.group.practic.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.security.core.GrantedAuthority;


@Entity
@Table(name = "role")
public class RoleEntity implements GrantedAuthority {

    private static final long serialVersionUID = 2978243562527482541L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column
    private String name;


    public RoleEntity() {
    }


    public RoleEntity(String name) {
        this.name = name;
    }


    public long getId() {
        return id;
    }


    public void setId(long id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return "Role{" + "name='" + name + '\'' + '}';
    }


    @Override
    public String getAuthority() {
        return "ROLE_" + name;
    }

}
