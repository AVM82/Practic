package com.group.practic.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.Objects;


@Entity
@Table(name = "reference_title")
public class ReferenceTitleEntity implements Serializable {

    private static final long serialVersionUID = 6370431962834166445L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    long id;

    String reference;

    String title;


    public ReferenceTitleEntity() {
    }


    public ReferenceTitleEntity(long id, String reference, String title) {
        this.id = id;
        this.reference = reference;
        this.title = title;
    }


    @Override
    public int hashCode() {
        return Objects.hash(reference, title);
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ReferenceTitleEntity other = (ReferenceTitleEntity) obj;
        return this == other || (Objects.equals(reference, other.reference)
                && Objects.equals(title, other.title));
    }


    public long getId() {
        return id;
    }


    public void setId(long id) {
        this.id = id;
    }


    public String getReference() {
        return reference;
    }


    public void setReference(String reference) {
        this.reference = reference;
    }


    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
    }


    public String toString() {
        return "(" + id + ')' + reference + " = " + title;
    }

}
