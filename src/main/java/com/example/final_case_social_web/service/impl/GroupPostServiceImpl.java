package com.example.final_case_social_web.service.impl;

import com.example.final_case_social_web.model.GroupPost;
import com.example.final_case_social_web.repository.GroupPostRepository;
import com.example.final_case_social_web.service.GroupPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupPostServiceImpl implements GroupPostService {
    @Autowired
    private GroupPostRepository groupPostRepository;

    @Override
    public Iterable<GroupPost> findAll() {
        return groupPostRepository.findAll();
    }

    @Override
    public Optional<GroupPost> findById(Long id) {
        return groupPostRepository.findById(id);
    }

    @Override
    public GroupPost save(GroupPost groupPost) {
        return groupPostRepository.save(groupPost);
    }

    @Override
    public List<GroupPost> findAllPostByIdGroup(Long idGroup) {
        return groupPostRepository.findAllPostByIdGroup(idGroup);
    }

    @Override
    public List<GroupPost> findAllPostWaiting(Long idGroup) {
        return groupPostRepository.findAllPostWaiting(idGroup);
    }

    @Override
    public void deleteGroupPost(GroupPost groupPost) {
        groupPostRepository.delete(groupPost);
    }
}
