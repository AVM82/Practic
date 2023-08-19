package com.group.practic.repository;

import com.group.practic.entity.StateEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface StateRepository extends JpaRepository<StateEntity, Long> {

    List<StateEntity> findAllByCluster(int cluster);

    Optional<StateEntity> findAllByClusterAndName(int cluster, String name);

}
