package com.example.final_case_social_web.service.impl;

import com.example.final_case_social_web.common.Constants;
import com.example.final_case_social_web.model.ImageGroup;
import com.example.final_case_social_web.repository.ImageGroupRepository;
import com.example.final_case_social_web.service.ImageGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageGroupServiceImpl implements ImageGroupService {

    @Autowired
    private ImageGroupRepository imageGroupRepository;

    @Override
    public ImageGroup createImageGroupDefault(String image, Long idTheGroup, Long idUserUpLoad) {
        ImageGroup imageGroup = new ImageGroup();
        imageGroup.setStatus(Constants.STATUS_PUBLIC);
        imageGroup.setIdTheGroup(idTheGroup);
        imageGroup.setIdUserUpLoad(idUserUpLoad);
        imageGroup.setDeleteAt(null);
        return imageGroup;
    }

    @Override
    public void save(ImageGroup imageGroup) {
        imageGroupRepository.save(imageGroup);
    }
}
