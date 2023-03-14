package com.example.final_case_social_web.service.impl;

import com.example.final_case_social_web.dto.CommentDTO;
import com.example.final_case_social_web.model.Comment;
import com.example.final_case_social_web.repository.CommentRepository;
import com.example.final_case_social_web.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public Iterable<Comment> findAll() {
        return commentRepository.findAll();
    }

    @Override
    public Optional<Comment> findById(Long id) {
        return commentRepository.findById(id);
    }

    @Override
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> findAllByUserId(Long id) {
        return commentRepository.findAllByUserId(id);
    }

    @Override
    public List<Comment> getCommentByIdPost(Long id) {
        return commentRepository.getCommentByIdPost(id);
    }

    @Override
    public List<Comment> getCommentTrue() {
        return commentRepository.getCommentTrue();
    }

    @Override
    public CommentDTO mapper(Comment comment) {
        return modelMapper.map(comment, CommentDTO.class);
    }

    @Override
    public void create(Comment comment) {
        comment.setNumberLike(0L);
        comment.setNumberDisLike(0L);
        comment.setCreateAt(LocalDateTime.now());
        comment.setDelete(false);
        comment.setEditAt(null);
    }

    @Override
    public void saveAll(List<Comment> comments) {
        commentRepository.saveAll(comments);
    }

    @Override
    public List<Comment> findAllByIdIn(List<Long> inputList) {
        return commentRepository.findAllByIdIn(inputList);
    }

    @Override
    public List<Comment> findAllByPostIdIn(List<Long> post_id) {
        return commentRepository.findAllByPostIdInAndDeleteAtIsNull(post_id);
    }
}
