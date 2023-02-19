package com.example.final_case_social_web.service;

import com.example.final_case_social_web.model.GroupPost;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GroupPostService extends GeneralService<GroupPost> {

    List<GroupPost> findAllPostByIdGroup(@Param("idGroup") Long idGroup);

    List<GroupPost> findAllPostWaiting(@Param("idGroup") Long idGroup);
}
