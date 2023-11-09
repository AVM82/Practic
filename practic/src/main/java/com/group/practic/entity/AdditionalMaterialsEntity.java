package com.group.practic.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "additional_materials")
public class AdditionalMaterialsEntity implements Serializable {

    private static final long serialVersionUID = -7620809512419827291L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    long id;

    @ManyToOne
    CourseEntity course;

    int number;

    @NotBlank
    String name;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "add_mats_refs", joinColumns = @JoinColumn(name = "add_mat_id"),
            inverseJoinColumns = @JoinColumn(name = "ref_id"))
    private Set<ReferenceTitleEntity> refs = new HashSet<>();


    public AdditionalMaterialsEntity() {}


    public AdditionalMaterialsEntity(long id, CourseEntity course, int number,
            @NotBlank String name, Set<ReferenceTitleEntity> refs) {
        this.id = id;
        this.course = course;
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


    public CourseEntity getCourse() {
        return course;
    }


    public void setCourse(CourseEntity course) {
        this.course = course;
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
