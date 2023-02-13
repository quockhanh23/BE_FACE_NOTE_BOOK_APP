package com.example.final_case_social_web.service;

import com.example.final_case_social_web.model.ImageGroup;

public interface ImageGroupService {

    ImageGroup createImageGroupDefault(String image, Long idTheGroup, Long idUserUpLoad);

    void save(ImageGroup imageGroup);
}
