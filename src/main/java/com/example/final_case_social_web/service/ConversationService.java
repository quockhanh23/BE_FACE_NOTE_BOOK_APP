package com.example.final_case_social_web.service;

import com.example.final_case_social_web.model.Conversation;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConversationService extends GeneralService<Conversation> {

    Iterable<Conversation> findAll();

    List<Conversation> findAllByIdSender(Long idSender);

    List<Conversation> listConversationByIdUserNotFriend(@Param("idUser") Long idUser);

    List<Conversation> getConversationBySenderIdOrReceiverId(@Param("senderId") Long senderId,
                                                             @Param("receiverId") Long receiverId);

    void delete(Conversation entity);
}
