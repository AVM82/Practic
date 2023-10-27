package com.group.practic.repository;

import com.group.practic.entity.FeedbackEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface FeedbackRepository extends JpaRepository<FeedbackEntity, Long> {

    void deleteById(long id);

    @Modifying
    @Transactional
    @Query("UPDATE FeedbackEntity f SET f.likes = f.likes + 1 WHERE f.id = :id")
    void incrementLikesById(Long id);

    @Modifying
    @Transactional
    @Query("UPDATE FeedbackEntity f SET f.likes = f.likes - 1 WHERE f.id = :id")
    void decrementLikesById(Long id);

}
