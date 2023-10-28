package com.group.practic.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Entity
@Table(name = "chapter_parts")
public class ChapterPartEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    long id;

    @ManyToOne
    @JsonIgnore
    ChapterEntity chapter;

    int number;

    @OneToMany(mappedBy = "chapterPart", cascade = CascadeType.ALL)
    @OrderBy("number")
    Set<SubChapterEntity> subChapters = new HashSet<>();

    @NotBlank
    String praxisPurpose;

    @OneToMany(mappedBy = "chapterPart", cascade = CascadeType.ALL)
    @OrderBy("number")
    Set<PraxisEntity> praxis = new HashSet<>();

    @OneToMany(mappedBy = "chapterPart", cascade = CascadeType.ALL)
    @OrderBy("number")
    Set<AdditionalEntity> additionals = new HashSet<>();


    public ChapterPartEntity() {
    }


    public ChapterPartEntity(ChapterEntity chapter, int number, @NotBlank String praxisPurpose) {
        this.chapter = chapter;
        this.number = number;
        this.praxisPurpose = praxisPurpose;
    }


    public ChapterPartEntity(long id, ChapterEntity chapter, int number,
            Set<SubChapterEntity> subChapters, @NotBlank String praxisPurpose,
            Set<PraxisEntity> praxis, Set<AdditionalEntity> additionals) {
        super();
        this.id = id;
        this.chapter = chapter;
        this.number = number;
        this.subChapters = subChapters;
        this.praxisPurpose = praxisPurpose;
        this.praxis = praxis;
        this.additionals = additionals;
    }


    @Override
    public int hashCode() {
        return Objects.hash(number, praxisPurpose);
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ChapterPartEntity other = (ChapterPartEntity) obj;
        return this == other
                || (number == other.number && Objects.equals(praxisPurpose, other.praxisPurpose));
    }


    public long getId() {
        return id;
    }


    public void setId(long id) {
        this.id = id;
    }


    public ChapterEntity getChapter() {
        return chapter;
    }


    public void setChapter(ChapterEntity chapter) {
        this.chapter = chapter;
    }


    public int getNumber() {
        return number;
    }


    public void setNumber(int number) {
        this.number = number;
    }


    public Set<SubChapterEntity> getSubChapters() {
        return subChapters;
    }


    public void setSubChapters(Set<SubChapterEntity> subChapters) {
        this.subChapters = subChapters;
    }


    public String getPraxisPurpose() {
        return praxisPurpose;
    }


    public void setPraxisPurpose(String praxisPurpose) {
        this.praxisPurpose = praxisPurpose;
    }


    public Set<PraxisEntity> getPraxis() {
        return praxis;
    }


    public void setPraxis(Set<PraxisEntity> praxis) {
        this.praxis = praxis;
    }


    public Set<AdditionalEntity> getAdditionals() {
        return additionals;
    }


    public void setAdditionals(Set<AdditionalEntity> additionals) {
        this.additionals = additionals;
    }

}
