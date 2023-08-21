package com.group.practic.service;

import com.group.practic.entity.StateEntity;
import com.group.practic.repository.StateRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class StateService {

    @Autowired
    StateRepository stateRepository;


    public List<StateEntity> get() {
        return stateRepository.findAll();
    }


    public Optional<StateEntity> get(long id) {
        return stateRepository.findById(id);
    }


    public List<StateEntity> get(int cluster) {
        return stateRepository.findAllByCluster(cluster);
    }


    public Optional<StateEntity> get(int cluster, String name) {
        return stateRepository.findAllByClusterAndName(cluster, name);
    }


    public Optional<StateEntity> create(StateEntity stateEntity) {
        return Optional.ofNullable(stateRepository.saveAndFlush(stateEntity));
    }


    public Optional<StateEntity> create(int uniqueId, String name) {
        return Optional.ofNullable(stateRepository.saveAndFlush(new StateEntity(uniqueId, name)));
    }


    public Optional<StateEntity> update(long id, int cluster) {
        Optional<StateEntity> stateEntity = get(id);
        if (stateEntity.isPresent()) {
            stateEntity.get().setCluster(cluster);
            return Optional.ofNullable(stateRepository.saveAndFlush(stateEntity.get()));
        }
        return Optional.empty();
    }


    public Optional<StateEntity> update(long id, String name) {
        Optional<StateEntity> stateEntity = get(id);
        if (stateEntity.isPresent()) {
            stateEntity.get().setName(name);
            return Optional.ofNullable(stateRepository.saveAndFlush(stateEntity.get()));
        }
        return Optional.empty();
    }


    public Optional<StateEntity> update(int cluster, String oldName, String newName) {
        Optional<StateEntity> stateEntity = get(cluster, oldName);
        if (stateEntity.isPresent()) {
            stateEntity.get().setName(newName);
            return Optional.ofNullable(stateRepository.saveAndFlush(stateEntity.get()));
        }
        return Optional.empty();
    }


    public Optional<StateEntity> addChange(long id, long transitionId) {
        Optional<StateEntity> stateEntityUpdatable = stateRepository.findById(id);
        Optional<StateEntity> stateEntityTransition = stateRepository.findById(transitionId);
        return stateEntityUpdatable.isPresent() && stateEntityTransition.isPresent()
                && stateEntityUpdatable.get().addChangeTo(stateEntityTransition.get())
                        ? Optional.ofNullable(stateRepository.saveAndFlush(stateEntityUpdatable.get()))
                        : Optional.empty();
    }


    public boolean allowChange(StateEntity stateEntity, String name) {
        return stateEntity.hasName(name);
    }


    public boolean allowChange(long id, String name) {
        Optional<StateEntity> stateEntity = get(id);
        if (stateEntity.isPresent()) {
            return allowChange(stateEntity.get(), name);
        }
        return false;
    }

}
