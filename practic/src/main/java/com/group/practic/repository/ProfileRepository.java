package com.group.practic.repository;

import com.group.practic.entity.PersonEntity;
import com.group.practic.entity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<ProfileEntity, Long> {


    Optional<ProfileEntity> findByPerson(PersonEntity person);
}
