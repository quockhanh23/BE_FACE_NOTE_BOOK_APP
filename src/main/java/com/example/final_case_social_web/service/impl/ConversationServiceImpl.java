package com.example.final_case_social_web.service.impl;

import com.example.final_case_social_web.model.Conversation;
import com.example.final_case_social_web.repository.ConversationRepository;
import com.example.final_case_social_web.service.ConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConversationServiceImpl implements ConversationService {
    @Autowired
    private ConversationRepository conversationRepository;

    @Override
    public Optional<Conversation> findById(Long id) {
        return conversationRepository.findById(id);
    }

    @Override
    public Conversation save(Conversation conversation) {
        return conversationRepository.save(conversation);
    }

    @Override
    public Iterable<Conversation> findAll() {
        return conversationRepository.findAll();
    }

    @Override
    public List<Conversation> findAllByIdSender(Long idSender) {
        return conversationRepository.findAllByIdSender(idSender);
    }

    @Override
    public List<Conversation> listConversationByIdUserNotFriend(Long idUser) {
        return conversationRepository.listConversationByIdUserNotFriend(idUser);
    }

    @Override
    public List<Conversation> getConversationBySenderIdOrReceiverId(Long senderId, Long receiverId) {
        return conversationRepository.getConversationBySenderIdOrReceiverId(senderId, receiverId);
    }

    @Override
    public void delete(Conversation entity) {
        conversationRepository.delete(entity);
    }

    @Override
    public boolean existsConversationsById(Long idConversation) {
        return conversationRepository.existsConversationsById(idConversation);
    }
}
