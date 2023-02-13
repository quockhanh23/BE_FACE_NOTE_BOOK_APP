package com.example.final_case_social_web.service;

import com.example.final_case_social_web.model.TheGroup;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TheGroupService extends GeneralService<TheGroup> {

    List<TheGroup> findByIdUserCreate(Long idUserCreate);

    List<TheGroup> findAllGroup(@Param("idUserCreate") Long idUserCreate);

    List<TheGroup> findAllByIdIn(List<Long> id);

    List<TheGroup> searchAllByGroupNameAndType(@Param("searchText") String searchText, @Param("idUser") Long idUser);
}
