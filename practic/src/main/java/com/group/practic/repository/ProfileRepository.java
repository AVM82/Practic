package com.group.practic.repository;

import com.group.practic.entity.PersonEntity;
import com.group.practic.entity.ProfileEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProfileRepository extends JpaRepository<ProfileEntity, Long> {


    Optional<ProfileEntity> findByPerson(PersonEntity person);
}
