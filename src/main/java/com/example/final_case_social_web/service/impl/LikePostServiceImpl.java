package com.example.final_case_social_web.service.impl;

import com.example.final_case_social_web.model.LikePost;
import com.example.final_case_social_web.repository.LikePostRepository;
import com.example.final_case_social_web.service.LikePostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LikePostServiceImpl implements LikePostService {
    @Autowired
    private LikePostRepository likePostRepository;

    @Override
    public Optional<LikePost> findById(Long id) {
        return likePostRepository.findById(id);
    }

    @Override
    public LikePost save(LikePost likePost) {
        return likePostRepository.save(likePost);
    }

    @Override
    public List<LikePost> findLike(Long idPost, Long idUser) {
        return likePostRepository.findLike(idPost, idUser);
    }

    @Override
    public List<LikePost> findAllLikeByPostId(Long idPost) {
        return likePostRepository.findAllByPostIdAndUserLikeIsNotNull(idPost);
    }

    @Override
    public List<LikePost> findAllByPostIdIn(List<Long> post_id) {
        return likePostRepository.findAllByPostIdInAndUserLikeIsNotNull(post_id);
    }

    @Override
    public void delete(LikePost entity) {
        likePostRepository.delete(entity);
    }
}
