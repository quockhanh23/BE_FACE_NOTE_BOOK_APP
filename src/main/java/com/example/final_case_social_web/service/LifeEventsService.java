package com.example.final_case_social_web.service;

import com.example.final_case_social_web.model.LifeEvents;

import java.util.List;

public interface LifeEventsService extends GeneralService<LifeEvents> {

    void delete(LifeEvents entity);

    List<LifeEvents> findLifeEventsByUserId(Long idUser);
}
