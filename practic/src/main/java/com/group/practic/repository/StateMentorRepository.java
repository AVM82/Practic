package com.group.practic.repository;

import com.group.practic.entity.StateMentorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StateMentorRepository  extends JpaRepository<StateMentorEntity, Long> {

    StateMentorEntity findByMentorId(long mentorId);

    StateMentorEntity findBySlug(String slug);
    
}
