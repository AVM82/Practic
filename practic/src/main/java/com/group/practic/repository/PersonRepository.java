package com.group.practic.repository;

import com.group.practic.entity.PersonEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PersonRepository extends JpaRepository<PersonEntity, Long> {

    List<PersonEntity> findAllByInactiveAndBan(boolean inactive, boolean ban);

    List<PersonEntity> findAllByNameAndInactiveAndBan(String name, boolean inactive, boolean ban);

    Optional<PersonEntity> findAllByName(String name);

    Optional<PersonEntity> findByDiscord(String discord);

    Optional<PersonEntity> findByLinkedin(String linkedin);

    Optional<PersonEntity> findPersonEntityByEmail(String email);

    Optional<PersonEntity> findById(Long id);

}
