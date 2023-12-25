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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "sub_chapters")
@Getter
@Setter
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
    private Set<ReferenceTitleEntity> refs = new HashSet<>();

    @OneToMany(mappedBy = "subChapter", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @OrderBy("number")
    private List<SubSubChapterEntity> subSubChapters = new ArrayList<>();

    private List<String> skills = new ArrayList<>();


    public SubChapterEntity() {}


    public SubChapterEntity(ChapterPartEntity chapterPart, int number, String name,
            List<String> skills, Set<ReferenceTitleEntity> refs) {
        this.chapterPart = chapterPart;
        this.number = number;
        this.name = name;
        this.skills = skills;
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
                && Objects.equals(skills, other.skills) && Objects.equals(refs, other.refs));
    }


    public SubChapterEntity update(SubChapterEntity sub) {
        this.name = sub.name;
        this.skills = sub.skills;
        this.refs = sub.refs;
        return this;
    }

}
