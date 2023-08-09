package com.group.practic.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

@Entity
@Data
public class SkillEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    private String name;

    @ManyToMany(fetch = FetchType.EAGER,
            cascade = CascadeType.ALL)
    @JoinTable(name = "skills_chapters",
            joinColumns = @JoinColumn(name = "skill_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "chapter_id", referencedColumnName = "id"))
    Collection<ChapterEntity> chapters = new ArrayList<>();

    public SkillEntity(String name) {
        this.name = name;
    }

    public SkillEntity() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SkillEntity)) return false;
        SkillEntity that = (SkillEntity) o;
        return getId() == that.getId() && Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName());
    }


}
