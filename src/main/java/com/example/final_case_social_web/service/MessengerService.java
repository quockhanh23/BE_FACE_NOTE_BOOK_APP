package com.example.final_case_social_web.service;

import com.example.final_case_social_web.model.Conversation;
import com.example.final_case_social_web.model.Messenger;
import com.example.final_case_social_web.model.User;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessengerService extends GeneralService<Messenger> {

    List<Messenger> findAllByConversationOrderById(Conversation conversation);

    void delete(Messenger entity);

    void saveAll(List<Messenger> messengers);

    List<Messenger> findImageByConversation(@Param("idConversation") Long idConversation);

    List<Messenger> findAllByConversation_IdAndContentNotNullOrderByIdDesc(Long conversation_id);

    List<Messenger> findAllByConversation_Id(Long id);

    List<Messenger> lastMessage(@Param("idConversation") Long idConversation);

    Messenger createDefaultMessage(Messenger messenger, User user, String type, String status);

    List<Messenger> lastTimeMessage(@Param("idConversation") Long idConversation);

    List<Messenger> findAllByConversation_IdIn(List<Long> conversation_id);

    List<Messenger> searchMessage(@Param("searchText") String searchText, @Param("idConversation") Long idConversation);
}
