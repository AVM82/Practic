package com.group.practic.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Entity
@Table(name = "sub_chapters")
public class SubChapterEntity implements Serializable {

    private static final long serialVersionUID = 1700674118330571946L;

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
    @JoinTable(name = "sub_chapters_refs", joinColumns = @JoinColumn(name = "sub_chapter_id"),
            inverseJoinColumns = @JoinColumn(name = "ref_id"))
    private Set<ReferenceTitleEntity> refs = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "sub_chapters_sub_sub_chapter",
            joinColumns = @JoinColumn(name = "sub_chapter_id"),
            inverseJoinColumns = @JoinColumn(name = "sub_sub_chapter_id"))
    @OrderBy("number")
    private Set<SubSubChapterEntity> subSubChapters = new HashSet<>();


    public SubChapterEntity() {}


    public SubChapterEntity(ChapterPartEntity chapterPart, int number, String name) {
        this.chapterPart = chapterPart;
        this.number = number;
        this.name = name;
    }


    public SubChapterEntity(long id, ChapterPartEntity chapterPart, int number, String name,
            Set<ReferenceTitleEntity> refs) {
        this.id = id;
        this.chapterPart = chapterPart;
        this.number = number;
        this.name = name;
        this.refs = refs;
    }


    @Override
    public int hashCode() {
        return Objects.hash(name, number, refs, subSubChapters);
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        SubChapterEntity other = (SubChapterEntity) obj;
        return this == other || (Objects.equals(name, other.name) && number == other.number
                && Objects.equals(refs, other.refs));
    }


    public long getId() {
        return id;
    }


    public void setId(long id) {
        this.id = id;
    }


    public ChapterPartEntity getChapterPart() {
        return chapterPart;
    }


    public void setChapterPart(ChapterPartEntity chapterPart) {
        this.chapterPart = chapterPart;
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


    public Set<SubSubChapterEntity> getSubSubChapters() {
        return subSubChapters;
    }


    public void setSubSubChapters(Set<SubSubChapterEntity> subSubChapters) {
        this.subSubChapters = subSubChapters;
    }


    public void addSubSubChapter(SubSubChapterEntity subSubChapter) {
        subSubChapters.add(subSubChapter);
    }


    public boolean removeSubSubChapter(SubSubChapterEntity subSubChapter) {
        return subSubChapters.remove(subSubChapter);
    }

}
