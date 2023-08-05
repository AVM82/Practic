package com.group.practic.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class CourseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;

    Long inactive;

    @ManyToOne(fetch = FetchType.LAZY)
    MentorEntity mentor;

    @ManyToOne(fetch = FetchType.EAGER)
    CourseType courseType;

    String name;

    String description;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private Set<StudentOnCourse> students = new HashSet<>();

    public CourseEntity(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
