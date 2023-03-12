package com.example.final_case_social_web.controller;

import com.example.final_case_social_web.common.Common;
import com.example.final_case_social_web.common.Constants;
import com.example.final_case_social_web.dto.UserDTO;
import com.example.final_case_social_web.dto.UserNotificationDTO;
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
    private FollowWatchingRepository followWatchingRepository;
    @Autowired
    private NotificationService notificationService;

    // Danh sách người lạ ở phần gợi ý chung
    @GetMapping("/allPeople")
    public ResponseEntity<?> listPeople(@RequestParam Long idUser) {
        List<User> listPeople = userService.listPeople(idUser);
        if (!CollectionUtils.isEmpty(listPeople)) {
            listPeople.removeIf(item -> item.getId().equals(idUser));
        }
        List<UserNotificationDTO> list = friendRelationService.listUser(listPeople);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/friendCheck")
    public ResponseEntity<?> friendCheck(@RequestParam Long idUser) {
        List<FriendRelation> friendRelations = friendRelationService.listRequest2(idUser);
        if (CollectionUtils.isEmpty(friendRelations)) {
            friendRelations = new ArrayList<>();
        }
        return new ResponseEntity<>(friendRelations, HttpStatus.OK);
    }

    @GetMapping("/friendWaiting")
    public ResponseEntity<?> friendWaiting(@RequestParam Long idFriend, @RequestParam Long idLogin) {
        List<FriendRelation> friendRelations = friendRelationService.friendWaiting(idFriend, idLogin);
        if (CollectionUtils.isEmpty(friendRelations)) {
            friendRelations = new ArrayList<>();
        }
        return new ResponseEntity<>(friendRelations, HttpStatus.OK);
    }

    @GetMapping("/allFriend")
    public ResponseEntity<?> allFriend(@RequestParam Long idFriend, @RequestParam Long idLogin) {
        List<FriendRelation> friendRelations = friendRelationService.allFriend(idFriend, idLogin);
        if (CollectionUtils.isEmpty(friendRelations)) {
            friendRelations = new ArrayList<>();
        }
        return new ResponseEntity<>(friendRelations, HttpStatus.OK);
    }

    // Danh sách những người đã gửi lời mời kết bạn
    @GetMapping("/findAllListRequestAddFriendById")
    public ResponseEntity<List<?>> findAllListRequestAddFriendById(@RequestParam Long idUser) {
        List<FriendRelation> friendRelations = friendRelationService.findAllListRequestAddFriendById(idUser);
        List<UserDTO> userDTOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(friendRelations)) {
            List<Long> idFriend = friendRelations.stream()
                    .map(item -> item.getUserLogin().getId()).distinct().collect(Collectors.toList());
            List<User> userList = userService.findAllByIdIn(idFriend);
            userList.removeIf(item -> item.getId().equals(idUser));
            userDTOList = friendRelationService.listResult(userList);
        }
        return new ResponseEntity<>(userDTOList, HttpStatus.OK);
    }

    // Danh sách bạn đã gửi lời mời kết bạn
    @GetMapping("/listRequest")
    public ResponseEntity<?> listRequest(@RequestParam Long idUser) {
        final double startTime = System.currentTimeMillis();
        List<FriendRelation> friendRelationList = friendRelationService.listRequest(idUser);
        List<UserDTO> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(friendRelationList)) {
            List<Long> idFriend = friendRelationList.stream()
                    .map(FriendRelation::getIdFriend).distinct().collect(Collectors.toList());
            List<User> userList = userService.findAllByIdIn(idFriend);
            list = friendRelationService.listResult(userList);
        }
        final double elapsedTimeMillis = System.currentTimeMillis();
        log.info(Constants.MESSAGE_STRIKE_THROUGH);
        Common.executionTime(startTime, elapsedTimeMillis);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    // Danh sách bạn bè
    @GetMapping("/listFriend")
    public ResponseEntity<?> listFriend(@RequestParam Long idUser) {
        List<UserDTO> userDTOList = userService.listFriend(idUser);
        return new ResponseEntity<>(userDTOList, HttpStatus.OK);
    }

    // Danh sách bạn bè giới hạn
    @GetMapping("/listFriendShowAvatarLimit")
    public ResponseEntity<?> listFriendShowAvatarLimit(@RequestParam Long idUser) {
        List<User> listFriend = userService.allFriendByUserId(idUser);
        List<UserDTO> userDTOList = listFriend.stream()
                .map(x -> new UserDTO(x.getId(), x.getFullName(), x.getAvatar(), x.getCover()))
                .limit(5)
                .collect(Collectors.toList());
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
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (user.get().getStatus().equals(Constants.STATUS_BANED)) {
            return new ResponseEntity<>(HttpStatus.LOCKED);
        }
        if (!optionalFriendRelation.isPresent()) {
            FriendRelation friendRelation = friendRelationService.createDefaultStatusWaiting();
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
            FriendRelation friendRelation = friendRelationService.createDefaultStatusWaiting();
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

    // Đồng ý kết bạn, Hủy kết bạn, Hủy yêu cầu kết bạn
    @DeleteMapping("/actionRequestFriend")
    public ResponseEntity<?> acceptRequestFriend(@RequestParam Long idUser,
                                                 @RequestParam Long idFriend,
                                                 @RequestParam String type) {
        Optional<FriendRelation> optionalFriendRelation = friendRelationService.findByIdUserAndIdFriend(idUser, idFriend);
        Optional<FriendRelation> optionalFriendRelation2 = friendRelationService.findByIdUserAndIdFriend(idFriend, idUser);
        if (!optionalFriendRelation.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (!optionalFriendRelation2.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (Objects.equals(idUser, idFriend)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<User> user = userService.findById(idFriend);
        if (!user.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Optional<User> user2 = userService.findById(idUser);
        if (!user2.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if ("accept".equalsIgnoreCase(type)) {
            if (user.get().getStatus().equals(Constants.STATUS_BANED)) {
                return new ResponseEntity<>(HttpStatus.LOCKED);
            }
            friendRelationService.saveAction(optionalFriendRelation.get(), optionalFriendRelation2.get(), Constants.FRIEND);
            List<FollowWatching> followWatchingList = followWatchingRepository.findOne(idUser, idFriend);
            if (!CollectionUtils.isEmpty(followWatchingList)) {
                followWatchingList.get(0).setStatus(Constants.FRIEND);
            }
            String title = Constants.Notification.TITLE_AGREE_FRIEND;
            String typeNotification = Constants.Notification.TYPE_FRIEND;
            Notification notification = notificationService.createDefault(user.get(), user2.get(), title, idFriend, typeNotification);
            notificationService.save(notification);
        }
        if ("delete".equalsIgnoreCase(type)) {
            friendRelationService.saveAction(optionalFriendRelation.get(), optionalFriendRelation2.get(), Constants.NO_FRIEND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 10 gợi ý kết bạn
    @GetMapping("/friendSuggestion")
    public ResponseEntity<?> everyone(@RequestParam Long idUser) {
        List<User> listSuggestion = userService.friendSuggestion(idUser);
        List<UserNotificationDTO> list = friendRelationService.listUser(listSuggestion);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}
