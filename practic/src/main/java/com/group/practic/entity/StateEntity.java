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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;


@Entity
@Table(name = "state", uniqueConstraints = {
        @UniqueConstraint(name = "UniqueState", columnNames = { "cluster", "name" }) })
public class StateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    long id;

    int cluster;

    @NotBlank
    @Column(unique = true, nullable = false)
    String name;

    @OneToMany(cascade = CascadeType.ALL)
    Set<StateEntity> changes = new HashSet<>();


    public StateEntity() {
    }


    public StateEntity(int cluster, String name) {
        this.cluster = cluster;
        this.name = name;
    }


    public StateEntity(long id, int cluster, String name, Set<StateEntity> changes) {
        this.id = id;
        this.cluster = cluster;
        this.name = name;
        this.changes = changes;
    }


    @Override
    public int hashCode() {
        return Objects.hash(changes, id, name, cluster);
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        StateEntity other = (StateEntity) obj;
        return this == other || (cluster == other.cluster && Objects.equals(name, other.name));
    }


    public long getId() {
        return id;
    }


    public void setId(long id) {
        this.id = id;
    }


    public int getGroup() {
        return cluster;
    }


    public void setGroup(int cluster) {
        this.cluster = cluster;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public int getCluster() {
        return cluster;
    }


    public void setCluster(int cluster) {
        this.cluster = cluster;
    }


    public Set<StateEntity> getChanges() {
        return changes;
    }


    public void setChanges(Set<StateEntity> changes) {
        this.changes = changes;
    }


    public boolean addChange(StateEntity stateEntity) {
        if (cluster == stateEntity.cluster && !hasName(stateEntity.name)) {
            changes.add(stateEntity);
            return true;
        }
        return false;
    }


    public synchronized boolean removeChange(StateEntity stateEntity) {
        if (cluster == stateEntity.cluster) {
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
        return cluster == newState.cluster
                && (hasName(newState.name) || (backward && newState.hasName(this.name)));
    }


    public boolean hasName(String name) {
        for (StateEntity state : changes) {
            if (name.equals(state.name)) {
                return true;
            }

        }
        return false;
    }

}
