package com.group.practic.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;


@Entity
@Table(name = "quizes")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class QuizEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 4011669179341834806L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    long id;

    @OneToOne
    private ChapterEntity chapter;

    @OrderBy("number")
    @OneToMany(mappedBy = "quiz", cascade = CascadeType.MERGE)
    private List<QuestionEntity> questions;

}
