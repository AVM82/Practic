package com.group.practic.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Entity
@Table(name = "chapter_parts")
public class ChapterPartEntity implements Serializable {

    private static final long serialVersionUID = 2228357501851519506L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    long id;

    @ManyToOne
    @JsonIgnore
    ChapterEntity chapter;

    int number;

    @OneToMany(mappedBy = "chapterPart", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @OrderBy("number")
    private List<SubChapterEntity> subChapters = new ArrayList<>();

    @NotBlank
    String praxisPurpose;

    @OneToMany(mappedBy = "chapterPart", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @OrderBy("number")
    private List<PraxisEntity> praxis = new ArrayList<>();

    @OneToMany(mappedBy = "chapterPart", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @OrderBy("number")
    private List<AdditionalEntity> additionals = new ArrayList<>();


    public ChapterPartEntity() {}


    public ChapterPartEntity(ChapterEntity chapter, int number, @NotBlank String praxisPurpose) {
        this.chapter = chapter;
        this.number = number;
        this.praxisPurpose = praxisPurpose;
    }


    public ChapterPartEntity(long id, ChapterEntity chapter, int number,
            List<SubChapterEntity> subChapters, @NotBlank String praxisPurpose,
            List<PraxisEntity> praxis, List<AdditionalEntity> additionals) {
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


    public List<SubChapterEntity> getSubChapters() {
        return subChapters;
    }


    public void setSubChapters(List<SubChapterEntity> subChapters) {
        this.subChapters = subChapters;
    }


    public String getPraxisPurpose() {
        return praxisPurpose;
    }


    public void setPraxisPurpose(String praxisPurpose) {
        this.praxisPurpose = praxisPurpose;
    }


    public List<PraxisEntity> getPraxis() {
        return praxis;
    }


    public void setPraxis(List<PraxisEntity> praxis) {
        this.praxis = praxis;
    }


    public List<AdditionalEntity> getAdditionals() {
        return additionals;
    }


    public void setAdditionals(List<AdditionalEntity> additionals) {
        this.additionals = additionals;
    }

}
