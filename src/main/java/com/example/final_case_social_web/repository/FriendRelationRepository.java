package com.example.final_case_social_web.repository;

import com.example.final_case_social_web.model.FriendRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRelationRepository extends JpaRepository<FriendRelation, Long> {

    Optional<FriendRelation> findByIdUserAndIdFriend(Long idUser, Long idFriend);

    @Modifying
    @Query(value = "select * from friend_relation where id_user= :idUser and friend_relation.status_friend = 'waiting' and id_user != user_login_id;", nativeQuery = true)
    List<FriendRelation> findAllListRequestAddFriendById(@Param("idUser") Long idUser);

    @Modifying
    @Query(value = "select * from friend_relation where id_user= :idUser and friend_relation.status_friend = 'waiting' and id_user = user_login_id;", nativeQuery = true)
    List<FriendRelation> listRequest(@Param("idUser") Long idUser);

    @Modifying
    @Query(value = "select * from friend_relation where id_user= :idUser and friend_relation.status_friend = 'waiting'", nativeQuery = true)
    List<FriendRelation> listRequest2(@Param("idUser") Long idUser);

    @Modifying
    @Query(value = "select * from friend_relation where id_friend= :idFriend and user_login_id= :idLogin and friend_relation.status_friend = 'waiting'", nativeQuery = true)
    List<FriendRelation> agreeFriend(@Param("idFriend") Long idFriend, @Param("idLogin") Long idLogin);

    @Modifying
    @Query(value = "select * from friend_relation where id_friend= :idFriend and user_login_id= :idLogin and friend_relation.status_friend = 'Friend'", nativeQuery = true)
    List<FriendRelation> friend(@Param("idFriend") Long idFriend, @Param("idLogin") Long idLogin);
}
