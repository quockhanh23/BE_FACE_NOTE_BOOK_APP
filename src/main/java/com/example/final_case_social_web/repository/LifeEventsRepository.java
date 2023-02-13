package com.example.final_case_social_web.repository;

import com.example.final_case_social_web.model.LifeEvents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LifeEventsRepository extends JpaRepository<LifeEvents, Long> {

    List<LifeEvents> findLifeEventsByUserIdOrderByTimelineAsc(Long idUser);
}
