package com.example.final_case_social_web.service;

import com.example.final_case_social_web.model.ShortNews;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ShortNewsService extends GeneralService<ShortNews> {

    Iterable<ShortNews> findAll();

    void delete(ShortNews entity);

    void createDefaultShortNews(ShortNews shortNews);

    void saveAll(List<ShortNews> shortNews);

    boolean checkYear(int year);

    List<ShortNews> findAllShortNews();

    List<ShortNews> findAllShortNewsPublic();

    List<ShortNews> myShortNew(@Param("idUser") Long idUser);

    List<ShortNews> getListShortNewInTrash(@Param("idUser") Long idUser);
}
