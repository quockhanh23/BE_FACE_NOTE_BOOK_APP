package com.example.final_case_social_web.service.impl;

import com.example.final_case_social_web.model.LifeEvents;
import com.example.final_case_social_web.repository.LifeEventsRepository;
import com.example.final_case_social_web.service.LifeEventsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LifeEventsServiceImpl implements LifeEventsService {
    @Autowired
    LifeEventsRepository lifeEventsRepository;

    @Override
    public Optional<LifeEvents> findById(Long id) {
        return lifeEventsRepository.findById(id);
    }

    @Override
    public LifeEvents save(LifeEvents lifeEvents) {
        return lifeEventsRepository.save(lifeEvents);
    }

    @Override
    public void delete(LifeEvents entity) {
        lifeEventsRepository.delete(entity);
    }

    @Override
    public List<LifeEvents> findLifeEventsByUserId(Long idUser) {
        return lifeEventsRepository.findLifeEventsByUserIdOrderByTimelineAsc(idUser);
    }
}
