package com.group.practic.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "quizes")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class QuizEntity implements Serializable {

    private static final long serialVersionUID = 4011669179341834806L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    long id;

    @Column(name = "chapter_name")
    private String chapterName;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.MERGE)
    private List<QuestionEntity> questions;

}
