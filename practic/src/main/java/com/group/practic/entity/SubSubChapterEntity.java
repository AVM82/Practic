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


@Entity
@Table(name = "sub_sub_chapters")
public class SubSubChapterEntity implements Serializable {

    private static final long serialVersionUID = 67849951914297249L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    long id;

    @ManyToOne
    @JsonIgnore
    SubChapterEntity subChapter;

    int number;

    @NotBlank
    @Column(length = 1024)
    String name;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private Set<ReferenceTitleEntity> refs = new HashSet<>();


    public SubSubChapterEntity() {}


    public SubSubChapterEntity(long id, SubChapterEntity subChapter, int number, String name,
            Set<ReferenceTitleEntity> refs) {
        this.id = id;
        this.subChapter = subChapter;
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
        SubSubChapterEntity other = (SubSubChapterEntity) obj;
        return this == other || (Objects.equals(name, other.name) && number == other.number
                && Objects.equals(refs, other.refs));
    }


    public long getId() {
        return id;
    }


    public void setId(long id) {
        this.id = id;
    }


    public SubChapterEntity getSubChapter() {
        return subChapter;
    }


    public void setSubChapter(SubChapterEntity subChapter) {
        this.subChapter = subChapter;
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
