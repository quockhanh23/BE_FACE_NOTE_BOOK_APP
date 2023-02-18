package com.example.final_case_social_web.controller;

import com.example.final_case_social_web.common.Constants;
import com.example.final_case_social_web.model.*;
import com.example.final_case_social_web.notification.ResponseNotification;
import com.example.final_case_social_web.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@PropertySource("classpath:application.properties")
@CrossOrigin("*")
@RequestMapping("/api/groups")
@Slf4j
public class GroupRestController {
    @Autowired
    private GroupParticipantService groupParticipantService;
    @Autowired
    private TheGroupService theGroupService;
    @Autowired
    private GroupPostService groupPostService;
    @Autowired
    private UserService userService;
    @Autowired
    private ImageGroupService imageGroupService;

    // Danh sách nhóm đã vào
    @GetMapping("/groupJoined")
    public ResponseEntity<?> groupJoined(@RequestParam Long idUser) {
        List<GroupParticipant> groupParticipants = groupParticipantService.groupJoined(idUser);
        List<TheGroup> theGroupList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(groupParticipants)) {
            List<Long> listId = groupParticipants.stream()
                    .map(item -> item.getTheGroup().getId()).collect(Collectors.toList());
            theGroupList = theGroupService.findAllByIdIn(listId);
        }
        return new ResponseEntity<>(theGroupList, HttpStatus.OK);
    }

    // Các nhóm đã tạo
    @GetMapping("/findAllGroup")
    public ResponseEntity<?> findAllGroup(@RequestParam Long idUserCreate) {
        List<TheGroup> theGroups = theGroupService.findAllGroup(idUserCreate);
        if (CollectionUtils.isEmpty(theGroups)) {
            theGroups = new ArrayList<>();
        }
        return new ResponseEntity<>(theGroups, HttpStatus.OK);
    }

    // Danh sách những người đăng kí tham gia nhóm chưa được duyệt
    @GetMapping("/findAllUserStatusPendingApproval")
    public ResponseEntity<?> findAllGroupParticipantByTheGroupId(@RequestParam Long idTheGroup) {
        List<GroupParticipant> groupParticipants = groupParticipantService.findAllUserStatusPendingApproval(idTheGroup);
        if (CollectionUtils.isEmpty(groupParticipants)) {
            groupParticipants = new ArrayList<>();
        }
        return new ResponseEntity<>(groupParticipants, HttpStatus.OK);
    }

    @GetMapping("/findAllUserStatusApproved")
    public ResponseEntity<?> findAllUserStatusApproved(@RequestParam Long idTheGroup) {
        List<GroupParticipant> groupParticipants = groupParticipantService.findAllUserStatusApproved(idTheGroup);
        if (CollectionUtils.isEmpty(groupParticipants)) {
            groupParticipants = new ArrayList<>();
        }
        return new ResponseEntity<>(groupParticipants, HttpStatus.OK);
    }

    @GetMapping("/findGroupByIdUserCreate")
    public ResponseEntity<?> findGroupByIdUserCreate(@RequestParam Long idUserCreate) {
        List<TheGroup> listGroupByIdUserCreate = theGroupService.findByIdUserCreate(idUserCreate);
        if (CollectionUtils.isEmpty(listGroupByIdUserCreate)) {
            listGroupByIdUserCreate = new ArrayList<>();
        }
        return new ResponseEntity<>(listGroupByIdUserCreate, HttpStatus.OK);
    }

    @GetMapping("/{idGroup}")
    public ResponseEntity<?> getOne(@PathVariable Long idGroup) {
        Optional<TheGroup> theGroupOptional = theGroupService.findById(idGroup);
        if (!theGroupOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_GROUP, idGroup),
                    HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(theGroupOptional.get(), HttpStatus.OK);
    }

    // Tạo nhóm mới
    @PostMapping("/createGroup")
    public ResponseEntity<?> createGroup(@RequestBody TheGroup theGroup, @RequestParam Long idUser) {
        Optional<User> userOptional = userService.findById(idUser);
        if (!userOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_USER, idUser),
                    HttpStatus.NOT_FOUND);
        }
        theGroup.setCreateBy(userOptional.get().getFullName());
        theGroup.setIdUserCreate(userOptional.get().getId());
        theGroup.setCreateAt(new Date());
        theGroup.setNumberUser(0L);
        theGroup.setStatus(Constants.STATUS_PUBLIC);
        if (theGroup.getAvatarGroup() == null) {
            theGroup.setAvatarGroup(Constants.ImageDefault.DEFAULT_AVATAR_GROUP);
        }
        if (theGroup.getCoverGroup() == null) {
            theGroup.setCoverGroup(Constants.ImageDefault.DEFAULT_COVER_GROUP);
        }
        if (StringUtils.isEmpty(theGroup.getGroupName())) {
            return new ResponseEntity<>(ResponseNotification.responseMessageDataField("groupName"), HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isEmpty(theGroup.getType())) {
            return new ResponseEntity<>(ResponseNotification.responseMessageDataField("type"), HttpStatus.BAD_REQUEST);
        }
        theGroupService.save(theGroup);
        ImageGroup coverGroup = imageGroupService
                .createImageGroupDefault(theGroup.getCoverGroup(), theGroup.getId(), userOptional.get().getId());
        ImageGroup avatarGroup = imageGroupService
                .createImageGroupDefault(theGroup.getAvatarGroup(), theGroup.getId(), userOptional.get().getId());
        imageGroupService.save(coverGroup);
        imageGroupService.save(avatarGroup);
        GroupParticipant groupParticipant = groupParticipantService
                .createDefault(theGroup, userOptional.get(), Constants.GroupStatus.MANAGEMENT);
        groupParticipantService.save(groupParticipant);
        return new ResponseEntity<>(theGroup, HttpStatus.OK);
    }

    // Khóa nhóm
    @DeleteMapping("/lockGroup")
    public ResponseEntity<?> lockGroup(@RequestParam Long idGroup,
                                       @RequestParam Long idUser) {
        Optional<TheGroup> theGroupOptional = theGroupService.findById(idGroup);
        if (!theGroupOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_GROUP, idGroup),
                    HttpStatus.NOT_FOUND);
        }
        Optional<User> userOptional = userService.findById(idUser);
        if (!userOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_USER, idUser),
                    HttpStatus.NOT_FOUND);
        }
        if (userService.checkAdmin(idUser).toString().substring(17, 27).equals(Constants.Roles.ROLE_ADMIN)) {
            theGroupOptional.get().setStatus(Constants.STATUS_LOCK);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        if (theGroupOptional.get().getIdUserCreate().equals(userOptional.get().getId())) {
            theGroupOptional.get().setStatus(Constants.STATUS_DELETE);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    // Xóa nhóm
    @DeleteMapping("/deleteGroup")
    public ResponseEntity<?> deleteGroup(@RequestParam Long idGroup,
                                         @RequestParam Long idUser) {
        Optional<TheGroup> theGroupOptional = theGroupService.findById(idGroup);
        if (!theGroupOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_GROUP, idGroup),
                    HttpStatus.NOT_FOUND);
        }
        Optional<User> userOptional = userService.findById(idUser);
        if (!userOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_USER, idUser),
                    HttpStatus.NOT_FOUND);
        }
        if (theGroupOptional.get().getIdUserCreate().equals(userOptional.get().getId())) {
            theGroupOptional.get().setStatus(Constants.STATUS_DELETE);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    // Gửi yêu cầu tham gia nhóm
    @GetMapping("/createGroupParticipant")
    public ResponseEntity<?> createGroupParticipant(@RequestParam Long idUser, @RequestParam Long idTheGroup) {
        if (userService.checkUser(idUser) == null) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_USER, idUser),
                    HttpStatus.NOT_FOUND);
        }
        Optional<TheGroup> theGroupOptional = theGroupService.findById(idTheGroup);
        if (!theGroupOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_THE_GROUP, idTheGroup),
                    HttpStatus.NOT_FOUND);
        }
        Optional<User> userOptional = userService.findById(idUser);
        if (!userOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_USER, idUser),
                    HttpStatus.NOT_FOUND);
        }
        Iterable<GroupParticipant> groupParticipants = groupParticipantService.findAll();
        List<GroupParticipant> groupParticipantList = (List<GroupParticipant>) groupParticipants;
        if (groupParticipants != null) {
            for (GroupParticipant groupParticipant : groupParticipantList) {
                if (groupParticipant.getUser().getId().equals(idUser)
                        && groupParticipant.getTheGroup().getId().equals(idTheGroup)) {
                    return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
                }
            }
        }
        GroupParticipant groupParticipant = groupParticipantService
                .createDefault(theGroupOptional.get(), userOptional.get(), Constants.GroupStatus.STATUS_GROUP_PENDING);
        groupParticipantService.save(groupParticipant);
        return new ResponseEntity<>(groupParticipant, HttpStatus.OK);
    }

    // Quản trị viên đồng ý user tham gia nhóm
    @GetMapping("/acceptUserJoinGroup")
    public ResponseEntity<?> acceptUserJoinGroup(@RequestParam Long idAdminGroup,
                                                 @RequestParam Long idUser,
                                                 @RequestParam Long idGroup) {
        Optional<User> userOptional = userService.findById(idAdminGroup);
        if (!userOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_ADMIN_GROUP, idAdminGroup), HttpStatus.NOT_FOUND);
        }
        Optional<GroupParticipant> groupParticipant = groupParticipantService.findByUserIdAndTheGroupId(idUser, idGroup);
        if (!groupParticipant.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (userOptional.get().getId().equals(groupParticipant.get().getTheGroup().getIdUserCreate())) {
            groupParticipant.get().setStatus(Constants.GroupStatus.STATUS_GROUP_APPROVED);
            groupParticipantService.save(groupParticipant.get());
            return new ResponseEntity<>(groupParticipant, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @GetMapping("/check")
    public ResponseEntity<?> check(@RequestParam Long idUser, @RequestParam Long idGroup) {
        Optional<GroupParticipant> groupParticipant = groupParticipantService.findByUserIdAndTheGroupId(idUser, idGroup);
        if (!groupParticipant.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(groupParticipant, HttpStatus.OK);
    }

    // Quản trị viên từ chối user
    @GetMapping("/rejectUserJoinGroup")
    public ResponseEntity<?> rejectUserJoinGroup(@RequestParam Long idAdminGroup,
                                                 @RequestParam Long idUser,
                                                 @RequestParam Long idGroup) {
        Optional<User> userOptional = userService.findById(idAdminGroup);
        if (!userOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_ADMIN_GROUP, idAdminGroup), HttpStatus.NOT_FOUND);
        }
        Optional<GroupParticipant> groupParticipant = groupParticipantService.findByUserIdAndTheGroupId(idUser, idGroup);
        if (!groupParticipant.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (userOptional.get().getId().equals(groupParticipant.get().getTheGroup().getIdUserCreate())) {
            groupParticipant.get().setStatus(Constants.GroupStatus.STATUS_GROUP_REFUSE);
            groupParticipantService.save(groupParticipant.get());
            return new ResponseEntity<>(groupParticipant, HttpStatus.OK);
        }
        return new ResponseEntity<>(groupParticipant, HttpStatus.NOT_MODIFIED);
    }

    // Quản trị viên duyệt bài viết
    @GetMapping("/acceptUserUpPost")
    public ResponseEntity<?> acceptUserUpPost(@RequestParam Long idAdminGroup,
                                              @RequestParam Long idGroupPost) {
        Optional<User> userOptional = userService.findById(idAdminGroup);
        if (!userOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_ADMIN_GROUP, idAdminGroup), HttpStatus.NOT_FOUND);
        }
        Optional<GroupPost> groupPost = groupPostService.findById(idGroupPost);
        if (!groupPost.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        groupPost.get().setStatus(Constants.GroupStatus.STATUS_GROUP_APPROVED);
        return new ResponseEntity<>(groupPost, HttpStatus.NOT_MODIFIED);
    }

    // Quản trị viên từ chối duyệt bài viết
    @GetMapping("/rejectUserUpPost")
    public ResponseEntity<?> rejectUserUpPost(@RequestParam Long idAdminGroup,
                                              @RequestParam Long idGroupPost) {
        Optional<User> userOptional = userService.findById(idAdminGroup);
        if (!userOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_ADMIN_GROUP, idAdminGroup), HttpStatus.NOT_FOUND);
        }
        Optional<GroupPost> groupPost = groupPostService.findById(idGroupPost);
        if (!groupPost.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        groupPost.get().setStatus(Constants.GroupStatus.STATUS_GROUP_REFUSE);
        return new ResponseEntity<>(groupPost, HttpStatus.NOT_MODIFIED);
    }

    // Tạo bài viết trong nhóm
    @PostMapping("/createGroupPost")
    public ResponseEntity<?> createGroupPost(@RequestParam Long idUser,
                                             @RequestParam Long idTheGroup,
                                             @RequestBody GroupPost groupPost) {
        if (StringUtils.isEmpty(groupPost.getContent().trim())) {
            return new ResponseEntity<>(ResponseNotification.responseMessageDataField(Constants.DataField.CONTENT),
                    HttpStatus.BAD_REQUEST);
        }
        Optional<User> userOptional = userService.findById(idUser);
        if (!userOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_ADMIN_GROUP, idUser), HttpStatus.NOT_FOUND);
        }
        Optional<TheGroup> theGroupOptional = theGroupService.findById(idTheGroup);
        if (!theGroupOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_THE_GROUP, idTheGroup), HttpStatus.NOT_FOUND);
        }
        groupPost.setStatus(Constants.GroupStatus.STATUS_GROUP_PENDING);
        groupPost.setCreateBy(userOptional.get().getFullName());
        groupPost.setCreateAt(new Date());
        groupPost.setTheGroup(theGroupOptional.get());
        groupPostService.save(groupPost);
        if (!StringUtils.isEmpty(groupPost.getImage())) {
            ImageGroup imageGroup = imageGroupService
                    .createImageGroupDefault(groupPost.getImage(), theGroupOptional.get().getId(), userOptional.get().getId());
            imageGroupService.save(imageGroup);
        }
        return new ResponseEntity<>(groupPost, HttpStatus.OK);
    }

    @GetMapping("/searchAllByGroupNameAndType")
    public ResponseEntity<?> searchAllByGroupNameAndType(String search, @RequestParam Long idUser) {
        Optional<User> userOptional = userService.findById(idUser);
        if (!userOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_USER, idUser), HttpStatus.NOT_FOUND);
        }
        List<TheGroup> theGroupList = theGroupService.searchAllByGroupNameAndType(search, idUser);
        return new ResponseEntity<>(theGroupList, HttpStatus.OK);
    }
}
