package com.example.final_case_social_web.service.impl;

import com.example.final_case_social_web.common.Constants;
import com.example.final_case_social_web.model.ShortNews;
import com.example.final_case_social_web.repository.ShortNewsRepository;
import com.example.final_case_social_web.service.ShortNewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ShortNewsServiceImpl implements ShortNewsService {
    @Autowired
    private ShortNewsRepository shortNewsRepository;

    @Override
    public Iterable<ShortNews> findAll() {
        return shortNewsRepository.findAll();
    }

    @Override
    public Optional<ShortNews> findById(Long id) {
        return shortNewsRepository.findById(id);
    }

    @Override
    public ShortNews save(ShortNews shortNews) {
        return shortNewsRepository.save(shortNews);
    }

    @Override
    public void delete(ShortNews entity) {
        shortNewsRepository.delete(entity);
    }

    @Override
    public void createShortNews(ShortNews shortNews) {
        shortNews.setCreateAt(new Date());
        shortNews.setToDay(new Date());
        shortNews.setExpired(3);
        shortNews.setRemaining(3);
        if (shortNews.getImage() == null || shortNews.getImage().equals(Constants.BLANK)) {
            shortNews.setImage(Constants.ImageDefault.DEFAULT_IMAGE_SHORT_NEW);
        }
    }

    @Override
    public void saveAll(List<ShortNews> shortNews) {
        shortNewsRepository.saveAll(shortNews);
    }

    @Override
    public boolean checkYear(int year) {
        if (year % 400 == 0) {
            return true;
        } else if (year % 100 == 0) {
            return false;
        } else if (year % 4 == 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<ShortNews> findAllShortNews() {
        return shortNewsRepository.findAllShortNews();
    }

    @Override
    public List<ShortNews> findAllShortNewsPublic() {
        return shortNewsRepository.findAllShortNewsPublic();
    }

    @Override
    public List<ShortNews> myShortNew(Long idUser) {
        return shortNewsRepository.myShortNew(idUser);
    }
}
