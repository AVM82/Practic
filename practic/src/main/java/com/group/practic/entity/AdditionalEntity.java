package com.group.practic.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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
@Table(name = "additionals")
@Getter
@Setter
public class AdditionalEntity implements Serializable {

    private static final long serialVersionUID = -9098533325684649496L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    long id;

    @ManyToOne
    @JsonIgnore
    ChapterPartEntity chapterPart;

    int number;

    @NotBlank
    @Column(length = 1024)
    String name;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private Set<ReferenceTitleEntity> refs = new HashSet<>();


    public AdditionalEntity() {}


    public AdditionalEntity(long id, ChapterPartEntity chapterPart, int number,
            @NotBlank String name, Set<ReferenceTitleEntity> refs) {
        super();
        this.id = id;
        this.chapterPart = chapterPart;
        this.number = number;
        this.name = name;
        this.refs = refs;
    }


    @Override
    public int hashCode() {
        return Objects.hash(name, number, refs);
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        AdditionalEntity other = (AdditionalEntity) obj;
        return this == obj || (Objects.equals(name, other.name) && number == other.number);
    }


    public AdditionalEntity update(AdditionalEntity additional) {
        name = additional.name;
        return this;
    }

}
