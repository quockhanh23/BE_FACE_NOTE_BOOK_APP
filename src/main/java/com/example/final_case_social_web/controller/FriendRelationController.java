package com.example.final_case_social_web.controller;

import com.example.final_case_social_web.common.Constants;
import com.example.final_case_social_web.dto.UserDTO;
import com.example.final_case_social_web.model.FollowWatching;
import com.example.final_case_social_web.model.FriendRelation;
import com.example.final_case_social_web.model.Notification;
import com.example.final_case_social_web.model.User;
import com.example.final_case_social_web.notification.ResponseNotification;
import com.example.final_case_social_web.repository.FollowWatchingRepository;
import com.example.final_case_social_web.service.FriendRelationService;
import com.example.final_case_social_web.service.NotificationService;
import com.example.final_case_social_web.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@PropertySource("classpath:application.properties")
@CrossOrigin("*")
@RequestMapping("/api/friends")
@Slf4j
public class FriendRelationController {
    @Autowired
    private FriendRelationService friendRelationService;
    @Autowired
    private UserService userService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private FollowWatchingRepository followWatchingRepository;
    @Autowired
    private NotificationService notificationService;

    // Danh sách người lạ ở phần gợi ý chung
    @GetMapping("/allPeople")
    public ResponseEntity<?> listPeople(@RequestParam Long idUser) {
        List<User> listPeople = userService.listPeople(idUser);
        if (CollectionUtils.isEmpty(listPeople)) {
            listPeople = new ArrayList<>();
        }
        listPeople.removeIf(item -> item.getId().equals(idUser));
        return new ResponseEntity<>(listPeople, HttpStatus.OK);
    }

    @GetMapping("/friendCheck")
    public ResponseEntity<?> friendCheck(@RequestParam Long idUser) {
        List<FriendRelation> friendRelations = friendRelationService.listRequest2(idUser);
        if (CollectionUtils.isEmpty(friendRelations)) {
            friendRelations = new ArrayList<>();
        }
        return new ResponseEntity<>(friendRelations, HttpStatus.OK);
    }

    @GetMapping("/agree")
    public ResponseEntity<?> agree(@RequestParam Long idFriend, @RequestParam Long idLogin) {
        List<FriendRelation> friendRelations = friendRelationService.agreeFriend(idFriend, idLogin);
        if (CollectionUtils.isEmpty(friendRelations)) {
            friendRelations = new ArrayList<>();
        }
        return new ResponseEntity<>(friendRelations, HttpStatus.OK);
    }

    @GetMapping("/friend")
    public ResponseEntity<?> friend(@RequestParam Long idFriend, @RequestParam Long idLogin) {
        List<FriendRelation> friendRelations = friendRelationService.friend(idFriend, idLogin);
        if (CollectionUtils.isEmpty(friendRelations)) {
            friendRelations = new ArrayList<>();
        }
        return new ResponseEntity<>(friendRelations, HttpStatus.OK);
    }

    // Danh sách những người đã gửi lời mời kết bạn
    @GetMapping("/findAllListRequestAddFriendById")
    public ResponseEntity<List<?>> findAllListRequestAddFriendById(@RequestParam Long idUser) {
        List<FriendRelation> friendRelations = friendRelationService.findAllListRequestAddFriendById(idUser);
        if (CollectionUtils.isEmpty(friendRelations)) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        }
        List<Long> idFriend = friendRelations.stream()
                .map(item -> item.getUserLogin().getId()).distinct().collect(Collectors.toList());
        List<User> userList = userService.findAllByIdIn(idFriend);
        if (CollectionUtils.isEmpty(friendRelations)) {
            userList = new ArrayList<>();
        }
        userList.removeIf(item -> item.getId().equals(idUser));
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    // Danh sách bạn đã gửi lời mời kết bạn
    @GetMapping("/listRequest")
    public ResponseEntity<?> listRequest(@RequestParam Long idUser) {
        List<FriendRelation> friendRelationList = friendRelationService.listRequest(idUser);
        if (CollectionUtils.isEmpty(friendRelationList)) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        }
        List<Long> idFriend = friendRelationList.stream()
                .map(FriendRelation::getIdFriend).distinct().collect(Collectors.toList());
        List<User> userList = userService.findAllByIdIn(idFriend);
        userList.removeIf(item -> item.getId().equals(idUser));
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    // Danh sách bạn bè
    @GetMapping("/listFriend")
    public ResponseEntity<?> listFriend(@RequestParam Long idUser) {
        List<User> listFriend = userService.allFriendByUserId(idUser);
        List<UserDTO> userDTOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(listFriend)) {
            listFriend.forEach(user -> {
                UserDTO userDTO = new UserDTO();
                BeanUtils.copyProperties(user, userDTO);
                userDTOList.add(userDTO);
            });
            for (int i = 0; i < userDTOList.size(); i++) {
                List<User> friendOfFriend = userService.allFriendByUserId(userDTOList.get(i).getId());
                List<User> mutualFriends = new ArrayList<>();
                if (!CollectionUtils.isEmpty(friendOfFriend)) {
                    List<Long> listId = userDTOList.stream().map(UserDTO::getId).collect(Collectors.toList());
                    for (int j = 0; j < listId.size(); j++) {
                        Long id = listId.get(j);
                        friendOfFriend.stream().filter(item -> item.getId().equals(id))
                                .findFirst().ifPresent(mutualFriends::add);
                    }
                }
                userDTOList.get(i).setMutualFriends(mutualFriends.size());
            }
        }
        return new ResponseEntity<>(userDTOList, HttpStatus.OK);
    }

    // Danh sách bạn bè giới hạn
    @GetMapping("/listFriendShowAvatarLimit")
    public ResponseEntity<?> listFriendShowAvatarLimit(@RequestParam Long idUser) {
        List<User> listFriend = userService.allFriendByUserId(idUser);
        List<UserDTO> userDTOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(listFriend)) {
            for (User user : listFriend) {
                UserDTO userDTO = modelMapper.map(user, UserDTO.class);
                userDTOList.add(userDTO);
                if (userDTOList.size() == 5) {
                    break;
                }
            }
        }
        return new ResponseEntity<>(userDTOList, HttpStatus.OK);
    }

    // Số lượng bạn chung
    @GetMapping("/mutualFriends")
    public ResponseEntity<?> mutualFriends(@RequestParam Long idUserLogin, @RequestParam Long idUser) {
        List<User> listFriend = userService.allFriendByUserId(idUserLogin);
        List<User> friendOfFriend = userService.allFriendByUserId(idUser);
        List<Long> mutualFriends = new ArrayList<>();
        if (listFriend != null && friendOfFriend != null) {
            for (User value : listFriend) {
                for (User user : friendOfFriend) {
                    if (value.getId().equals(user.getId())) {
                        mutualFriends.add(user.getId());
                    }
                }
            }
        }
        return new ResponseEntity<>(mutualFriends.size(), HttpStatus.OK);
    }

    // Gửi lời mời kết bạn
    @DeleteMapping("/sendRequestFriend")
    public ResponseEntity<?> senRequestFriend(@RequestParam Long idUser, @RequestParam Long idFriend) {
        Optional<FriendRelation> optionalFriendRelation = friendRelationService.findByIdUserAndIdFriend(idUser, idFriend);
        Optional<FriendRelation> optionalFriendRelation2 = friendRelationService.findByIdUserAndIdFriend(idFriend, idUser);
        Optional<User> user = userService.findById(idFriend);
        Optional<User> user2 = userService.findById(idUser);
        if (!user.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_USER, idUser),
                    HttpStatus.NOT_FOUND);
        }
        if (!user2.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_USER, idUser),
                    HttpStatus.NOT_FOUND);
        }
        if (Objects.equals(idUser, idFriend)) {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
        if (user.get().getStatus().equals(Constants.STATUS_BANED)) {
            return new ResponseEntity<>(HttpStatus.LOCKED);
        }
        if (!optionalFriendRelation.isPresent()) {
            FriendRelation friendRelation = friendRelationService.create();
            friendRelation.setUserLogin(user2.get());
            friendRelation.setIdFriend(idFriend);
            friendRelation.setIdUser(idUser);
            friendRelation.setFriend(user2.get());
            friendRelationService.save(friendRelation);
        } else {
            optionalFriendRelation.get().setStatusFriend(Constants.WAITING);
            optionalFriendRelation.get().setUserLogin(user2.get());
            friendRelationService.save(optionalFriendRelation.get());
        }
        if (!optionalFriendRelation2.isPresent()) {
            FriendRelation friendRelation = friendRelationService.create();
            friendRelation.setFriend(user2.get());
            friendRelation.setIdUser(idFriend);
            friendRelation.setIdFriend(idUser);
            friendRelation.setUserLogin(user2.get());
            friendRelationService.save(friendRelation);
        } else {
            optionalFriendRelation2.get().setStatusFriend(Constants.WAITING);
            optionalFriendRelation2.get().setUserLogin(user2.get());
            friendRelationService.save(optionalFriendRelation2.get());
        }
        String title = Constants.Notification.TITLE_SEND_REQUEST_FRIEND;
        String type = Constants.Notification.TYPE_FRIEND;
        Notification notification = notificationService.createDefault(user.get(), user2.get(), title, idFriend, type);
        notificationService.save(notification);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Đồng ý kết bạn
    @DeleteMapping("/acceptRequestFriend")
    public ResponseEntity<?> acceptRequestFriend(@RequestParam Long idUser, @RequestParam Long idFriend) {
        Optional<FriendRelation> optionalFriendRelation = friendRelationService.findByIdUserAndIdFriend(idUser, idFriend);
        Optional<FriendRelation> optionalFriendRelation2 = friendRelationService.findByIdUserAndIdFriend(idFriend, idUser);
        if (!optionalFriendRelation.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (!optionalFriendRelation2.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (Objects.equals(idUser, idFriend)) {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
        Optional<User> user = userService.findById(idFriend);
        if (!user.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Optional<User> user2 = userService.findById(idUser);
        if (!user2.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (user.get().getStatus().equals(Constants.STATUS_BANED)) {
            return new ResponseEntity<>(HttpStatus.LOCKED);
        }
        optionalFriendRelation.get().setStatusFriend(Constants.FRIEND);
        optionalFriendRelation2.get().setStatusFriend(Constants.FRIEND);
        friendRelationService.save(optionalFriendRelation.get());
        friendRelationService.save(optionalFriendRelation2.get());
        List<FollowWatching> followWatchingList = followWatchingRepository.findOne(idUser, idFriend);
        if (!CollectionUtils.isEmpty(followWatchingList)) {
            followWatchingList.get(0).setStatus(Constants.FRIEND);
        }
        String title = Constants.Notification.TITLE_AGREE_FRIEND;
        String type = Constants.Notification.TYPE_FRIEND;
        Notification notification = notificationService.createDefault(user.get(), user2.get(), title, idFriend, type);
        notificationService.save(notification);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Hủy kết bạn, Hủy yêu cầu kết bạn
    @DeleteMapping("/deleteFriendRelation")
    public ResponseEntity<?> deleteFriendRelation(@RequestParam Long idUser,
                                                  @RequestParam Long idFriend) {
        Optional<FriendRelation> optionalFriendRelation = friendRelationService.findByIdUserAndIdFriend(idUser, idFriend);
        Optional<FriendRelation> optionalFriendRelation2 = friendRelationService.findByIdUserAndIdFriend(idFriend, idUser);
        if (!optionalFriendRelation.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (!optionalFriendRelation2.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (Objects.equals(idUser, idFriend)) {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
        optionalFriendRelation.get().setStatusFriend(Constants.NO_FRIEND);
        optionalFriendRelation2.get().setStatusFriend(Constants.NO_FRIEND);
        friendRelationService.save(optionalFriendRelation.get());
        friendRelationService.save(optionalFriendRelation2.get());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 10 gợi ý kết bạn
    @GetMapping("/friendSuggestion")
    public ResponseEntity<?> everyone(@RequestParam Long idUser) {
        List<User> listSuggestion = userService.friendSuggestion(idUser);
        List<UserDTO> listSuggestionUser = new ArrayList<>();
        if (!CollectionUtils.isEmpty(listSuggestion)) {
            listSuggestion.forEach(user -> {
                UserDTO userDTO = new UserDTO();
                BeanUtils.copyProperties(user, userDTO);
                listSuggestionUser.add(userDTO);
            });
        }
        return new ResponseEntity<>(listSuggestionUser, HttpStatus.OK);
    }
}
