package com.example.final_case_social_web.service;

import com.example.final_case_social_web.model.FriendRelation;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendRelationService extends GeneralService<FriendRelation> {

    Optional<FriendRelation> findByIdUserAndIdFriend(Long idUser, Long idFriend);

    List<FriendRelation> findAllListRequestAddFriendById(Long idUser);

    List<FriendRelation> listRequest(@Param("idUser") Long idUser);

    List<FriendRelation> listRequest2(@Param("idUser") Long idUser);

    List<FriendRelation> agreeFriend(@Param("idFriend") Long idFriend, @Param("idLogin") Long idLogin);

    List<FriendRelation> friend(@Param("idFriend") Long idFriend, @Param("idLogin") Long idLogin);

    FriendRelation create();
}
