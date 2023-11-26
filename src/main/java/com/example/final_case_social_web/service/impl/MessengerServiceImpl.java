package com.example.final_case_social_web.service.impl;

import com.example.final_case_social_web.common.Constants;
import com.example.final_case_social_web.model.Conversation;
import com.example.final_case_social_web.model.Messenger;
import com.example.final_case_social_web.model.User;
import com.example.final_case_social_web.repository.MessengerRepository;
import com.example.final_case_social_web.service.MessengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class MessengerServiceImpl implements MessengerService {
    @Autowired
    private MessengerRepository messengerRepository;

    @Override
    public Optional<Messenger> findById(Long id) {
        return messengerRepository.findById(id);
    }

    @Override
    public Messenger save(Messenger messenger) {
        return messengerRepository.save(messenger);
    }


    @Override
    public List<Messenger> findAllByConversationOrderById(Conversation conversation) {
        return messengerRepository.findAllByConversationOrderById(conversation);
    }

    @Override
    public void delete(Messenger entity) {
        messengerRepository.delete(entity);
    }

    @Override
    public void saveAll(List<Messenger> messengers) {
        messengerRepository.saveAll(messengers);
    }

    @Override
    public List<Messenger> findImageByConversation(Long idConversation) {
        return messengerRepository.findImageByConversation(idConversation);
    }

    @Override
    public List<Messenger> findAllByConversation_IdAndContentNotNullOrderByIdDesc(Long conversation_id) {
        return messengerRepository.findAllByConversation_IdAndContentNotNullOrderByIdDesc(conversation_id);
    }

    @Override
    public List<Messenger> findAllByConversation_Id(Long id) {
        return messengerRepository.findAllByConversation_Id(id);
    }

    @Override
    public List<Messenger> lastMessage(Long idConversation) {
        return messengerRepository.lastMessage(idConversation);
    }

    @Override
    public Messenger createDefaultMessage(Messenger messenger, User user, String type, String status) {
        if (type.equals(Constants.RESPONSE)) {
            messenger.setFormat(Constants.RESPONSE);
        }
        if (type.equals(Constants.REQUEST)) {
            messenger.setFormat(Constants.REQUEST);
        }
        messenger.setCreateAt(new Date());
        messenger.setIdSender(user);
        return messenger;
    }

    @Override
    public List<Messenger> lastTimeMessage(Long idConversation) {
        return messengerRepository.lastTimeMessage(idConversation);
    }

    @Override
    public List<Messenger> findAllByConversation_IdIn(List<Long> conversation_id) {
        return messengerRepository.findAllByConversation_IdIn(conversation_id);
    }

    @Override
    public List<Messenger> searchMessage(String searchText, Long idConversation) {
        return messengerRepository.searchMessage(searchText, idConversation);
    }

}
