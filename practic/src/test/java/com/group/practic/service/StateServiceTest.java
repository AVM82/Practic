package com.group.practic.service;

import com.group.practic.entity.StateEntity;
import com.group.practic.repository.StateRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j

public class StateServiceTest {

    @Mock
    private StateRepository stateRepository;
    @InjectMocks
    private StateService stateService;
    @Mock

    StateEntity stateEntity;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    void testGetAllStates() {
        List<StateEntity> states = new ArrayList<>();
        states.add(new StateEntity(1, "State1"));
        states.add(new StateEntity(2, "State2"));
        when(stateRepository.findAll()).thenReturn(states);
        List<StateEntity> result = stateService.get();
        assertEquals(2, result.size());
        assertEquals("State1", result.get(0).getName());
        assertEquals("State2", result.get(1).getName());
    }

    @Test
    void testGetStateById() {
        long stateId = 1L;
        StateEntity state = new StateEntity((int) stateId, "State1");

        when(stateRepository.findById(stateId)).thenReturn(Optional.of(state));
        Optional<StateEntity> result = stateService.get(stateId);
        assertEquals(state, result.orElse(null));
    }

    @Test
    void testGetStatesByCluster() {
        int cluster = 1;
        List<StateEntity> states = new ArrayList<>();
        states.add(new StateEntity(1, "State1"));
        states.add(new StateEntity(2, "State2"));

        when(stateRepository.findAllByCluster(cluster)).thenReturn(states);
        List<StateEntity> result = stateService.get(cluster);
        assertEquals(2, result.size());
        assertEquals("State1", result.get(0).getName());
        assertEquals("State2", result.get(1).getName());
    }

    @Test
    void testGetStateByClusterAndName() {
        int cluster = 1;
        String name = "State1";
        StateEntity state = new StateEntity(1, name);
        when(stateRepository.findAllByClusterAndName(cluster, name)).thenReturn(Optional.of(state));
        Optional<StateEntity> result = stateService.get(cluster, name);
        assertEquals(name, result.get().getName());
    }

    @Test
    void testCreateStateEntity() {
        StateEntity state = new StateEntity(1, "State1");
        when(stateRepository.saveAndFlush(state)).thenReturn(state);
        Optional<StateEntity> result = stateService.create(state);
        assertEquals("State1", result.get().getName());
        assertEquals(1, result.get().getCluster());

    }

    @Test
    void testCreateStateWithUniqueIdAndName() {
        int uniqueId = 1;
        String name = "State1";
        StateEntity state = new StateEntity(uniqueId, name);
        state.setId(uniqueId);
        when(stateRepository.saveAndFlush(state)).thenReturn(state);

        Optional<StateEntity> result = stateService.create(uniqueId, name);
        assertEquals(name, result.get().getName());
        assertEquals(uniqueId, result.get().getId());
    }

    @Test
    void testAllowChange() {
        String expectedName = "State1";
        stateEntity.setName(expectedName);
        when(stateEntity.hasName(expectedName)).thenReturn(true);
        boolean allowChange = stateService.allowChange(stateEntity, expectedName);
        assertTrue(allowChange);
    }

    @Test
    void testDisallowChange() {
        String expectedName = "State1";
        StateEntity state = new StateEntity();
        state.setName(expectedName);
        boolean allowChange = stateService.allowChange(state, expectedName);
        assertFalse(allowChange);
    }
    @Test
    void testUpdateStateWithValidIdAndCluster() {
        long id = 1L;
        int newCluster = 10;

        StateEntity existingState = new StateEntity();
        existingState.setId(id);
        existingState.setCluster(5);

        when(stateRepository.findById(id)).thenReturn(Optional.of(existingState));
        when(stateRepository.saveAndFlush(existingState)).thenReturn(existingState);


        Optional<StateEntity> updatedState = stateService.update(id, newCluster);

        assertTrue(updatedState.isPresent());
        assertEquals(newCluster, updatedState.get().getCluster());
    }
    @Test
    void testUpdateStateWithInvalidId() {

        long id = 1L;
        int newCluster = 10;

        when(stateRepository.findById(id)).thenReturn(Optional.empty());
        Optional<StateEntity> updatedState = stateService.update(id, newCluster);
        assertFalse(updatedState.isPresent());
    }
    @Test
    void testUpdateStateWithValidIdAndName() {
        long id = 1L;
        String newName = "NewStateName";

        StateEntity existingState = new StateEntity();
        existingState.setId(id);
        existingState.setName("OldStateName");

        when(stateRepository.findById(id)).thenReturn(Optional.of(existingState));
        when(stateRepository.saveAndFlush(existingState)).thenReturn(existingState);

        Optional<StateEntity> updatedState = stateService.update(id, newName);

        assertTrue(updatedState.isPresent());
        assertEquals(newName, updatedState.get().getName());
    }
    @Test
    void testUpdateStateWithInvalidName() {
        long id = 1L;
        String newName = "NewStateName";

        StateEntity existingState = new StateEntity();
        existingState.setId(id);
        existingState.setName("OldStateName");

        when(stateRepository.findById(id)).thenReturn(Optional.of(existingState));
        when(stateRepository.saveAndFlush(existingState)).thenReturn(null);

        Optional<StateEntity> updatedState = stateService.update(id, newName);

        assertFalse(updatedState.isPresent());
    }
    @Test
    void testUpdateStateWithValidClusterAndNames() {
        int cluster = 1;
        String oldName = "OldStateName";
        String newName = "NewStateName";

        StateEntity existingState = new StateEntity();
        existingState.setCluster(cluster);
        existingState.setName(oldName);

        when(stateRepository.findAllByClusterAndName(cluster, oldName)).thenReturn(Optional.of(existingState));
        when(stateRepository.saveAndFlush(existingState)).thenReturn(existingState);

        Optional<StateEntity> updatedState = stateService.update(cluster, oldName, newName);

        assertTrue(updatedState.isPresent());
        assertEquals(newName, updatedState.get().getName());
    }
    @Test
    void testUpdateStateWithInvalidClusterAndNames() {
        int cluster = 1;
        String oldName = "OldStateName";
        String newName = "NewStateName";
        when(stateRepository.findAllByClusterAndName(cluster, oldName)).thenReturn(Optional.empty());
        Optional<StateEntity> updatedState = stateService.update(cluster, oldName, newName);
        assertFalse(updatedState.isPresent());
    }
    @Test
    void testAllowChangeWithValidIdAndName() {
        long id = 1L;
        String expectedName = "State1";
        stateEntity.setName(expectedName);
        when(stateEntity.hasName(expectedName)).thenReturn(true);
        when(stateRepository.findById(id)).thenReturn(Optional.of(stateEntity));
        when(stateEntity.hasName(expectedName)).thenReturn(true);

        boolean allowChange = stateService.allowChange(id, expectedName);
        assertTrue(allowChange);
    }
}