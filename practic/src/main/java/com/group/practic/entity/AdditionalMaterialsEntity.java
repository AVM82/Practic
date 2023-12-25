package com.group.practic.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "additional_materials")
@Getter
@Setter
public class AdditionalMaterialsEntity implements Serializable {

    private static final long serialVersionUID = -7620809512419827291L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    long id;

    @ManyToOne
    @JsonIgnore
    CourseEntity course;

    int number;

    @NotBlank
    String name;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
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

    
    public AdditionalMaterialsEntity update(AdditionalMaterialsEntity entity) {
        this.name = entity.name;
        this.number = entity.number;
        this.refs = entity.refs;
        return this;
    }
    
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        AdditionalMaterialsEntity other = (AdditionalMaterialsEntity) obj;
        return this == obj || (number == other.number && Objects.equals(name, other.name)
                && Objects.equals(refs, other.refs)); 
    }
    
}
