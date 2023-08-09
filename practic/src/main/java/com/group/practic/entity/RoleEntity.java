package com.group.practic.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "role")
public class RoleEntity {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column
  private String name;


  public RoleEntity() {}


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
  
}
