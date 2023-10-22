package com.group.practic.repository;

import com.group.practic.entity.ReferenceTitleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ReferenceTitleRepository extends JpaRepository<ReferenceTitleEntity, Long> {

    ReferenceTitleEntity findByReference(String reference);

}
