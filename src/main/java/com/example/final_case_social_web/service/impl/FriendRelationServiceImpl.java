package com.example.final_case_social_web.service.impl;

import com.example.final_case_social_web.common.Constants;
import com.example.final_case_social_web.model.FriendRelation;
import com.example.final_case_social_web.repository.FriendRelationRepository;
import com.example.final_case_social_web.service.FriendRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FriendRelationServiceImpl implements FriendRelationService {
    @Autowired
    private FriendRelationRepository friendRelationRepository;

    @Override
    public Iterable<FriendRelation> findAll() {
        return friendRelationRepository.findAll();
    }

    @Override
    public Optional<FriendRelation> findById(Long id) {
        return friendRelationRepository.findById(id);
    }

    @Override
    public FriendRelation save(FriendRelation friendRelation) {
        return friendRelationRepository.save(friendRelation);
    }

    @Override
    public Optional<FriendRelation> findByIdUserAndIdFriend(Long idUser, Long idFriend) {
        return friendRelationRepository.findByIdUserAndIdFriend(idUser, idFriend);
    }

    @Override
    public List<FriendRelation> findAllListRequestAddFriendById(Long idUser) {
        return friendRelationRepository.findAllListRequestAddFriendById(idUser);
    }

    @Override
    public List<FriendRelation> listRequest(Long idUser) {
        return friendRelationRepository.listRequest(idUser);
    }

    @Override
    public List<FriendRelation> listRequest2(Long idUser) {
        return friendRelationRepository.listRequest2(idUser);
    }

    @Override
    public List<FriendRelation> agreeFriend(Long idFriend, Long idLogin) {
        return friendRelationRepository.agreeFriend(idFriend, idLogin);
    }

    @Override
    public List<FriendRelation> friend(Long idFriend, Long idLogin) {
        return friendRelationRepository.friend(idFriend, idLogin);
    }

    @Override
    public FriendRelation create() {
        FriendRelation friendRelation = new FriendRelation();
        friendRelation.setStatusFriend(Constants.WAITING);
        return friendRelation;
    }
}
