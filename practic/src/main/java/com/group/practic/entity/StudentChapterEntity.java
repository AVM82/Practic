package com.group.practic.entity;

import com.group.practic.enumeration.ChapterState;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Table(name = "student_chapters")
@Getter
@Setter
@Entity
public class StudentChapterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    long id;

    @ManyToOne
    private StudentEntity student;

    @ManyToOne
    private ChapterEntity chapter;

    @Enumerated(EnumType.STRING)
    ChapterState state = ChapterState.NOT_STARTED;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private java.sql.Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private java.sql.Timestamp updatedAt;

    boolean reportOnce;

    @OneToMany
    Set<StudentPracticeEntity> practices;


    public StudentChapterEntity() {}


    public StudentChapterEntity(StudentEntity student, ChapterEntity chapter) {
        this.student = student;
        this.chapter = chapter;
    }

}
