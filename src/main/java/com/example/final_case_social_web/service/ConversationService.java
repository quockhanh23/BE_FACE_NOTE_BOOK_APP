package com.example.final_case_social_web.service;

import com.example.final_case_social_web.model.Conversation;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConversationService extends GeneralService<Conversation> {

    List<Conversation> findAllByIdSender(Long idSender);

    List<Conversation> listConversationByIdUserNotFriend(@Param("idUser") Long idUser);

    void delete(Conversation entity);
}
