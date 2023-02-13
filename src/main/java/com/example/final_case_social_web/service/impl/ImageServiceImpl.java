package com.example.final_case_social_web.service.impl;

import com.example.final_case_social_web.common.Constants;
import com.example.final_case_social_web.model.Image;
import com.example.final_case_social_web.model.User;
import com.example.final_case_social_web.repository.ImageRepository;
import com.example.final_case_social_web.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ImageServiceImpl implements ImageService {
    @Autowired
    private ImageRepository imageRepository;

    @Override
    public Iterable<Image> findAll() {
        return imageRepository.findAll();
    }

    @Override
    public Optional<Image> findById(Long id) {
        return imageRepository.findById(id);
    }

    @Override
    public Image save(Image image) {
        return imageRepository.save(image);
    }

    @Override
    public void delete(Image entity) {
        imageRepository.delete(entity);
    }

    @Override
    public List<Image> findAllImageByIdUser(Long idUser) {
        return imageRepository.findAllImageByIdUser(idUser);
    }

    @Override
    public Image createImageDefault(String image, User user) {
        Image image1 = new Image();
        image1.setLinkImage(image);
        image1.setStatus(Constants.STATUS_PUBLIC);
        image1.setDeleteAt(null);
        image1.setUser(user);
        return image1;
    }


    @Override
    public List<Image> findAllImageDeletedByUserId(Long idUser) {
        return imageRepository.findAllImageDeletedByUserId(idUser);
    }
}
