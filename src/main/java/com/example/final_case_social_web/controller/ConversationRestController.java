package com.example.final_case_social_web.controller;

import com.example.final_case_social_web.common.Constants;
import com.example.final_case_social_web.dto.MessagePhotoDTO;
import com.example.final_case_social_web.dto.MessengerDTO;
import com.example.final_case_social_web.model.Conversation;
import com.example.final_case_social_web.model.ConversationDeleteTime;
import com.example.final_case_social_web.model.Messenger;
import com.example.final_case_social_web.model.User;
import com.example.final_case_social_web.notification.ResponseNotification;
import com.example.final_case_social_web.repository.ConversationDeleteTimeRepository;
import com.example.final_case_social_web.service.ConversationService;
import com.example.final_case_social_web.service.MessengerService;
import com.example.final_case_social_web.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@PropertySource("classpath:application.properties")
@CrossOrigin("*")
@RequestMapping("/api/conversations")
@Slf4j
public class ConversationRestController {
    @Autowired
    private ConversationService conversationService;
    @Autowired
    private MessengerService messengerService;
    @Autowired
    private UserService userService;
    @Autowired
    private ConversationDeleteTimeRepository conversationDeleteTimeRepository;
    @Autowired
    private ModelMapper modelMapper;

    //   messengers2.sort((p1, p2) -> p2.getCount() - p1.getCount());

    // Tạo cuộc trò truyện
    @GetMapping("/createConversation")
    public ResponseEntity<?> createConversation(@RequestParam Long idSender,
                                                @RequestParam Long idReceiver) {
        Optional<User> userSender = userService.findById(idSender);
        if (!userSender.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_SENDER, idSender), HttpStatus.NOT_FOUND);
        }
        Optional<User> userReceiver = userService.findById(idReceiver);
        if (!userReceiver.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_RECEIVER, idReceiver), HttpStatus.NOT_FOUND);
        }
        Iterable<Conversation> conversations = conversationService.findAll();
        List<Conversation> conversationList = (List<Conversation>) conversations;
        if (conversations != null) {
            for (Conversation conversation : conversationList) {
                if ((conversation.getIdSender().getId().equals(idSender)
                        && conversation.getIdReceiver().getId().equals(idReceiver))
                        || (conversation.getIdSender().getId().equals(idReceiver)
                        && conversation.getIdReceiver().getId().equals(idSender))) {
                    return new ResponseEntity<>(conversation, HttpStatus.OK);
                }
            }
        }
        Conversation conversation = new Conversation();
        conversation.setIdSender(userSender.get());
        conversation.setIdReceiver(userReceiver.get());
        conversation.setCreateAt(new Date());
        conversationService.save(conversation);
        return new ResponseEntity<>(conversation, HttpStatus.OK);
    }

    @GetMapping("/searchMessage")
    public ResponseEntity<?> searchMessage(String search, @RequestParam Long idConversation) {
        List<Messenger> messengers = messengerService.searchMessage(search, idConversation);
        return new ResponseEntity<>(messengers, HttpStatus.OK);
    }

    @DeleteMapping("/deleteOneSide")
    public ResponseEntity<?> deleteOneSide(@RequestParam Long idUser, @RequestParam Long idConversation) {
        Optional<Conversation> conversation = conversationService.findById(idConversation);
        if (!conversation.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_CONVERSATION, idConversation),
                    HttpStatus.NOT_FOUND);
        }
        Optional<User> userOptional = userService.findById(idUser);
        if (!userOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_USER, idUser),
                    HttpStatus.NOT_FOUND);
        }
        ConversationDeleteTime conversationDeleteTime = new ConversationDeleteTime();
        conversationDeleteTime.setIdDelete(idUser);
        conversationDeleteTime.setIdConversation(idConversation);
        conversationDeleteTime.setTimeDelete(new Date());
        conversationDeleteTimeRepository.save(conversationDeleteTime);
        List<Messenger> messengers = messengerService.findAllByConversation_Id(idConversation);
        for (Messenger messenger : messengers) {
            if (messenger.getConversationDeleteTime().getId() == null) {
                messenger.setConversationDeleteTime(conversationDeleteTime);
            }
        }
        messengerService.saveAll(messengers);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/myMessenger")
    public ResponseEntity<?> myMessenger(@RequestParam Long idUser) {

        Optional<User> userOptional = userService.findById(idUser);
        if (!userOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_USER, idUser),
                    HttpStatus.NOT_FOUND);
        }
        List<Conversation> conversations = conversationService.findAllByIdSender(userOptional.get().getId());
        if (CollectionUtils.isEmpty(conversations)) {
            conversations = new ArrayList<>();
        }
        return new ResponseEntity<>(conversations, HttpStatus.OK);
    }

    @GetMapping("/messenger")
    public ResponseEntity<?> myMessenger(@RequestParam Long idUser, @RequestParam Long idConversation) {
        Optional<Conversation> conversation = conversationService.findById(idConversation);
        if (!conversation.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_CONVERSATION, idConversation), HttpStatus.NOT_FOUND);
        }
        Optional<User> userOptional = userService.findById(idUser);
        if (!userOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_USER, idUser),
                    HttpStatus.NOT_FOUND);
        }
        List<Messenger> messengers = messengerService.findAllByConversationOrderById(conversation.get());
        if (CollectionUtils.isEmpty(messengers)) {
            messengers = new ArrayList<>();
        }
        return new ResponseEntity<>(messengers, HttpStatus.OK);
    }

    // Tạo tin nhắn
    @PostMapping("/createMessengers")
    public ResponseEntity<?> createMessengers(@RequestParam Long idConversation,
                                              @RequestParam Long idUser,
                                              @RequestBody Messenger messenger) {
        Optional<Conversation> conversation = conversationService.findById(idConversation);
        if (!conversation.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_CONVERSATION, idConversation), HttpStatus.NOT_FOUND);
        }
        Optional<User> userOptional = userService.findById(idUser);
        if (!userOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_USER, idUser),
                    HttpStatus.NOT_FOUND);
        }
        if ((messenger.getContent() == null || messenger.getContent().equals(Constants.BLANK))
                && (messenger.getImage() == null || messenger.getImage().equals(Constants.BLANK))) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        messenger.setConversation(conversation.get());
        if (!Objects.equals(messenger.getConversation().getIdSender().getId(), userOptional.get().getId())
                && (messenger.getConversation().getIdReceiver().getId().equals(userOptional.get().getId()))) {
            Messenger messenger1 = messengerService.createDefaultMessage(messenger, userOptional.get(),
                    Constants.RESPONSE, Constants.ConversationStatus.STATUS_TWO);
            messengerService.save(messenger1);
            return new ResponseEntity<>(messenger, HttpStatus.OK);
        }
        if (!Objects.equals(messenger.getConversation().getIdReceiver().getId(), userOptional.get().getId())
                && messenger.getConversation().getIdSender().getId().equals(userOptional.get().getId())) {
            Messenger messenger1 = messengerService.createDefaultMessage(messenger, userOptional.get(),
                    Constants.REQUEST, Constants.ConversationStatus.STATUS_TWO);
            messengerService.save(messenger1);
            return new ResponseEntity<>(messenger, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @GetMapping("/findById")
    public ResponseEntity<?> findById(@RequestParam Long idConversation) {
        Optional<Conversation> conversation = conversationService.findById(idConversation);
        if (!conversation.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_CONVERSATION, idConversation), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(conversation, HttpStatus.OK);
    }

    @GetMapping("/findAllByConversationOrderById")
    public ResponseEntity<?> findAllByConversationOrderById(@RequestParam Long idConversation) {
        Optional<Conversation> conversation = conversationService.findById(idConversation);
        if (!conversation.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_CONVERSATION, idConversation), HttpStatus.NOT_FOUND);
        }
        List<Messenger> messengers = messengerService.findAllByConversationOrderById(conversation.get());
        if (CollectionUtils.isEmpty(messengers)) {
            messengers = new ArrayList<>();
        }
        return new ResponseEntity<>(messengers, HttpStatus.OK);
    }

    // Các ảnh đã gửi ở cuộc trò truyện
    @GetMapping("/getAllMessageHavePhoto")
    public ResponseEntity<?> getAllMessageHavePhoto(@RequestParam Long idConversation) {
        Optional<Conversation> conversation = conversationService.findById(idConversation);
        if (!conversation.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_CONVERSATION, idConversation), HttpStatus.NOT_FOUND);
        }
        List<MessagePhotoDTO> messengersHaveLink = new ArrayList<>();
        List<Messenger> messengers = messengerService.findImageByConversation(idConversation);
        if (!CollectionUtils.isEmpty(messengers)) {
            for (Messenger messenger : messengers) {
                MessagePhotoDTO messengerDTO = modelMapper.map(messenger, MessagePhotoDTO.class);
                messengersHaveLink.add(messengerDTO);
            }
        }
        return new ResponseEntity<>(messengersHaveLink, HttpStatus.OK);
    }

    // Xem link đã gửi ở cuộc trò truyện
    @GetMapping("/getAllMessageHaveLink")
    public ResponseEntity<?> findAllByConversation_IdAndContentNotNull(@RequestParam Long idConversation) {
        Set<String> messengersHaveLink = new HashSet<>();
        List<Messenger> messengers = messengerService.findAllByConversation_IdAndContentNotNullOrderByIdDesc(idConversation);
        if (messengers == null) {
            messengers = new ArrayList<>();
        }
        int count = 0;
        for (Messenger messenger : messengers) {
            if (messenger.getContent().length() > 8) {
                if (messenger.getContent().substring(0, 8).equals(Constants.Link.CHECK_LINK)
                        || messenger.getContent().substring(0, 5).equals(Constants.Link.CHECK_LINK_2)) {
                    messengersHaveLink.add(messenger.getContent());
                    count++;
                }
            }
            if (count == 10) {
                break;
            }
        }
        return new ResponseEntity<>(messengersHaveLink, HttpStatus.OK);
    }

    // Nhắn tin với người không có trong danh sách bạn bè
    @GetMapping("/listConversationNotFriend")
    public ResponseEntity<?> listConversationNotFriend(@RequestParam Long idUser) {
        List<Conversation> conversationList = conversationService.listConversationByIdUserNotFriend(idUser);
        List<Conversation> conversations = new ArrayList<>();
        if (!CollectionUtils.isEmpty(conversationList)) {
            List<Long> listId = conversationList.stream().map(Conversation::getId).collect(Collectors.toList());
            List<Messenger> messengers = messengerService.findAllByConversation_IdIn(listId);
            for (Long id : listId) {
                List<Messenger> messengerList = messengers.stream()
                        .filter(item -> item.getConversation().getId().equals(id)).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(messengerList)) {
                    conversationList.stream().filter(item -> item.getId().equals(id)).findFirst().ifPresent(conversations::add);
                }
            }
        }
        return new ResponseEntity<>(conversations, HttpStatus.OK);
    }

    @GetMapping("/messageNotFriend")
    public ResponseEntity<?> messageNotFriend(@RequestParam Long idConversation) {
        List<Messenger> messengers = messengerService.findAllByConversation_Id(idConversation);
        List<MessengerDTO> messengerList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(messengers)) {
            for (Messenger messenger : messengers) {
                MessengerDTO messengerDTO = new MessengerDTO();
                BeanUtils.copyProperties(messenger, messengerDTO);
                messengerDTO.setIdConversation(messenger.getConversation().getId());
                messengerList.add(messengerDTO);
            }
        }
        return new ResponseEntity<>(messengerList, HttpStatus.OK);
    }

    @GetMapping("/lastMessageIdSender")
    public ResponseEntity<?> lastMessage(@RequestParam Long idConversation) {
        List<Messenger> messengers = messengerService.lastMessage(idConversation);
        Long id = null;
        if (!CollectionUtils.isEmpty(messengers)) {
            id = messengers.get(0).getIdSender().getId();
        }
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @GetMapping("/lastMessage")
    public ResponseEntity<?> lastMessageTime(@RequestParam Long idConversation) {
        List<Messenger> messenger = messengerService.lastTimeMessage(idConversation);
        Date time = new Date();
        if (!CollectionUtils.isEmpty(messenger)) {
            time = messenger.get(0).getCreateAt();
        }
        return new ResponseEntity<>(time, HttpStatus.OK);
    }

    // Xóa 1 tin nhắn
    @DeleteMapping("/deleteMessenger")
    public ResponseEntity<?> deleteMessenger(@RequestParam Long idUser,
                                             @RequestParam Long idConversation,
                                             @RequestParam Long idMessenger) {
        Optional<User> userOptional = userService.findById(idUser);
        if (!userOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_USER, idUser), HttpStatus.NOT_FOUND);
        }
        Optional<Conversation> conversation = conversationService.findById(idConversation);
        if (!conversation.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_CONVERSATION, idConversation), HttpStatus.NOT_FOUND);
        }
        Optional<Messenger> messenger = messengerService.findById(idMessenger);
        if (!messenger.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_MESSAGE, idMessenger), HttpStatus.NOT_FOUND);
        }
        if (messenger.get().getConversation().getId().equals(idConversation)
                && conversation.get().getIdSender().getId().equals(idUser)) {
            messengerService.delete(messenger.get());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
}
