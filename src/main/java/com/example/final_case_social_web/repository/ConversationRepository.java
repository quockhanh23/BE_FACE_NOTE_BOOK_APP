package com.example.final_case_social_web.repository;

import com.example.final_case_social_web.model.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    @Modifying
    @Query(value = "select * from conversation where sender_id= :idUser or receiver_id= :idUser", nativeQuery = true)
    List<Conversation> findAllByIdSender(@Param("idUser") Long idUser);

    @Modifying
    @Query(value = "select *\n" +
            "from conversation\n" +
            "where (sender_id not in\n" +
            "      (select id_friend from friend_relation where id_user = :idUser and friend_relation.status_friend = 'Friend')\n" +
            "  and receiver_id not in\n" +
            "      (select id_friend from friend_relation where id_user = :idUser and friend_relation.status_friend = 'Friend'))\n" +
            "and (sender_id = :idUser or receiver_id = :idUser)", nativeQuery = true)
    List<Conversation> listConversationByIdUserNotFriend(@Param("idUser") Long idUser);

    void delete(Conversation entity);
}
