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
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "skills")
public class SkillEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    private String name;

    @ManyToMany(fetch = FetchType.EAGER,
            cascade = CascadeType.ALL)
    @JoinTable(name = "sub_chapters_skills",
            joinColumns = @JoinColumn(name = "skill_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "sub_chapter_id", referencedColumnName = "id"))
    Collection<SubChapterEntity> subChapters = new ArrayList<>();

    public void addChapter(SubChapterEntity subChapter) {
        subChapters.add(subChapter);
    }

    public SkillEntity(String name) {
        this.name = name;
    }

    public SkillEntity() {
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

    public Collection<SubChapterEntity> getSubChapters() {
        return subChapters;
    }

    public void setSubChapters(Collection<SubChapterEntity> subChapters) {
        this.subChapters = subChapters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SkillEntity that = (SkillEntity) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(subChapters, that.subChapters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName());
    }

    @Override
    public String toString() {
        return "SkillEntity{"
                +
                "id=" + id
                +
                ", name='" + name + '\''
                +
                ", subChapters=" + subChapters
                +
                '}';
    }
}
