package com.example.final_case_social_web.service;

import com.example.final_case_social_web.model.Image;
import com.example.final_case_social_web.model.User;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ImageService extends GeneralService<Image> {

    void delete(Image entity);

    List<Image> findAllImageByIdUser(@Param("idUser") Long idUser);

    Image createImageDefault(String image, User user);

    List<Image> findAllImageDeletedByUserId(@Param("idUser") Long idUser);
}
