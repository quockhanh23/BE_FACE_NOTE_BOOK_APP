package com.example.final_case_social_web.service;

import com.example.final_case_social_web.dto.CommentDTO;
import com.example.final_case_social_web.model.Comment;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentService extends GeneralService<Comment> {

    List<Comment> findAllByUserId(@Param("id") Long id);

    List<Comment> getCommentByIdPost(@Param("id") Long id);

    List<Comment> getCommentTrue();

    CommentDTO mapper(Comment comment);

    void create(Comment comment);

    void saveAll(List<Comment> comments);

    List<Comment> findAllByIdIn(@Param("inputList") List<Long> inputList);

    List<Comment> findAllByPostIdIn(List<Long> post_id);
}
