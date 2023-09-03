package com.group.practic.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "additional_materials")
public class AdditionalMaterialsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    long id;

    int number;

    @NotBlank
    String name;

    @OneToMany
    Set<ReferenceTitleEntity> refs = new HashSet<>();


    public AdditionalMaterialsEntity() {
    }


    public AdditionalMaterialsEntity(long id, int number, @NotBlank String name, 
            Set<ReferenceTitleEntity> refs) {
        this.id = id;
        this.number = number;
        this.name = name;
        this.refs = refs;
    }


    public long getId() {
        return id;
    }


    public void setId(long id) {
        this.id = id;
    }


    public int getNumber() {
        return number;
    }


    public void setNumber(int number) {
        this.number = number;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public Set<ReferenceTitleEntity> getRefs() {
        return refs;
    }


    public void setRefs(Set<ReferenceTitleEntity> refs) {
        this.refs = refs;
    }

}
