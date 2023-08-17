package com.group.practic.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;


@Entity
@Table(name = "state", uniqueConstraints = {
        @UniqueConstraint(name = "UniqueState", columnNames = { "inique_id", "name" }) })
public class StateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    long id;

    int uniqueId;

    @NotBlank
    @Column(unique = true, nullable = false)
    String name;

    @OneToMany(mappedBy = "state", cascade = CascadeType.ALL)
    Set<StateEntity> changes = new HashSet<>();


    public StateEntity() {
    }


    public StateEntity(int uniqueId, String name) {
        this.uniqueId = uniqueId;
        this.name = name;
    }


    public StateEntity(long id, int uniqueId, String name, Set<StateEntity> changes) {
        this.id = id;
        this.uniqueId = uniqueId;
        this.name = name;
        this.changes = changes;
    }


    @Override
    public int hashCode() {
        return Objects.hash(changes, id, name, uniqueId);
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass())
            return false;
        StateEntity other = (StateEntity) obj;
        return this == other || (uniqueId == other.uniqueId && Objects.equals(name, other.name));
    }


    public long getId() {
        return id;
    }


    public void setId(long id) {
        this.id = id;
    }


    public int getUnique() {
        return uniqueId;
    }


    public void setUnique(int uniqueId) {
        this.uniqueId = uniqueId;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public int getUniqueId() {
        return uniqueId;
    }


    public void setUniqueId(int uniqueId) {
        this.uniqueId = uniqueId;
    }


    public Set<StateEntity> getChanges() {
        return changes;
    }


    public void setChanges(Set<StateEntity> changes) {
        this.changes = changes;
    }


    public boolean addChange(StateEntity stateEntity) {
        if (uniqueId == stateEntity.uniqueId && !hasName(stateEntity.name)) {
            changes.add(stateEntity);
            return true;
        }
        return false;
    }


    public synchronized boolean removeChange(StateEntity stateEntity) {
        if (uniqueId == stateEntity.uniqueId) {
            Iterator<StateEntity> it = changes.iterator();
            while (it.hasNext()) {
                if (name.equals(it.next().name)) {
                    it.remove();
                    return true;
                }
            }
        }
        return false;
    }


    public boolean allowStateChange(StateEntity newState) {
        return allowStateChange(newState, false);
    }


    public boolean allowStateChange(StateEntity newState, boolean backward) {
        return uniqueId == newState.uniqueId
                && (hasName(newState.name) || (backward && newState.hasName(this.name)));
    }


    public boolean hasName(String name) {
        for (StateEntity state : changes) {
            if (name.equals(state.name))
                return true;
        }
        return false;
    }

}
