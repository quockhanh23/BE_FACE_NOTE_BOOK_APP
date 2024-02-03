package com.example.final_case_social_web.repository;

import com.example.final_case_social_web.dto.MessengerDTO2;
import com.example.final_case_social_web.model.Conversation;
import com.example.final_case_social_web.model.Messenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessengerRepository extends JpaRepository<Messenger, Long> {

    List<Messenger> findAllByConversationOrderById(Conversation conversation);

    List<Messenger> findAllByConversation_Id(Long id);

    List<Messenger> findAllByConversation_IdIn(List<Long> conversation_id);

    List<Messenger> findAllByConversation_IdAndContentNotNullOrderByIdDesc(Long conversation_id);

    void delete(Messenger entity);

    @Modifying
    @Query(value = "select * from messenger where image IS NOT NULL and image != '' and conversation_id = :idConversation", nativeQuery = true)
    List<Messenger> findImageByConversation(@Param("idConversation") Long idConversation);

    @Modifying
    @Query(value = "select * from messenger where conversation_id = :idConversation order by id desc limit 1", nativeQuery = true)
    Messenger lastMessage(@Param("idConversation") Long idConversation);

    @Query(value = "select create_at from messenger where conversation_id = :idConversation order by create_at desc limit 1", nativeQuery = true)
    Object lastTimeMessage(@Param("idConversation") Long idConversation);


    List<MessengerDTO2> findByConversation_Id(@Param("idConversation") Long idConversation);

    @Modifying
    @Query(value = "select *\n" +
            "from messenger\n" +
            "where conversation_id = :idConversation\n" +
            "  and content LIKE concat('%', :searchText, '%') order by create_at asc", nativeQuery = true)
    List<Messenger> searchMessage(@Param("searchText") String searchText, @Param("idConversation") Long idConversation);
}
