package com.example.final_case_social_web.service;

import com.example.final_case_social_web.dto.PostDTO;
import com.example.final_case_social_web.model.Post2;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostService extends GeneralService<Post2> {

    List<Post2> findAllPostByUser(@Param("id") Long id);

    List<Post2> allPost(Long id);

    void delete(Post2 entity);

    PostDTO mapper(Post2 post2);

    void create(Post2 post2);

    Post2 checkPost(Long idPost);

    void saveAll(List<Post2> post2List);

    List<PostDTO> changeDTO(List<Post2> post2List);

    List<Post2> findAllByUserIdAndDeleteTrue(Long user_id);

    List<PostDTO> filterListPost(List<Post2> post2List);
}
