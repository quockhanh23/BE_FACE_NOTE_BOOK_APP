package com.example.final_case_social_web.controller;

import com.example.final_case_social_web.common.Constants;
import com.example.final_case_social_web.common.MessageResponse;
import com.example.final_case_social_web.model.GroupPost;
import com.example.final_case_social_web.model.Post2;
import com.example.final_case_social_web.model.Saved;
import com.example.final_case_social_web.model.User;
import com.example.final_case_social_web.notification.ResponseNotification;
import com.example.final_case_social_web.repository.SavedRepository;
import com.example.final_case_social_web.service.GroupPostService;
import com.example.final_case_social_web.service.PostService;
import com.example.final_case_social_web.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@PropertySource("classpath:application.properties")
@CrossOrigin("*")
@RequestMapping("/api/saves")
@Slf4j
public class SavedRestController {
    @Autowired
    private SavedRepository savedRepository;
    @Autowired
    private GroupPostService groupPostService;
    @Autowired
    private UserService userService;
    @Autowired
    private PostService postService;

    // Danh sách đã lưu
    @GetMapping("/listSavedPost")
    public ResponseEntity<?> listSavedPost(@RequestParam Long idUser) {
        List<Saved> savedList = savedRepository.findAllSavedPost(idUser);
        if (CollectionUtils.isEmpty(savedList)) {
            savedList = new ArrayList<>();
        }
        return new ResponseEntity<>(savedList, HttpStatus.OK);
    }

    // Lưu trữ bài viết
    @GetMapping("/savePost")
    public ResponseEntity<?> savePost(@RequestParam Long idPost, @RequestParam Long idUser, @RequestParam String type) {
        Optional<Post2> postOptional = postService.findById(idPost);
        Optional<GroupPost> groupPost = groupPostService.findById(idPost);
        if ("post".equalsIgnoreCase(type)) {
            if (!postOptional.isPresent()) {
                return new ResponseEntity<>(ResponseNotification.
                        responseMessage(Constants.IdCheck.ID_POST, idPost), HttpStatus.NOT_FOUND);
            }
        }
        if ("groupPost".equalsIgnoreCase(type)) {
            if (!groupPost.isPresent()) {
                return new ResponseEntity<>(ResponseNotification.
                        responseMessage(Constants.IdCheck.ID_POST, idPost), HttpStatus.NOT_FOUND);
            }
        }
        Optional<User> userOptional = userService.findById(idUser);
        if (!userOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_USER, idUser),
                    HttpStatus.NOT_FOUND);
        }
        List<Saved> savedList = savedRepository.findAll();
        for (int i = 0; i < savedList.size(); i++) {
            if (savedList.get(i).getIdUser().equals(userOptional.get().getId())
                    && savedList.get(i).getIdPost().equals(idPost)) {
                if (savedList.get(i).getStatus().equals(Constants.STATUS_SAVED)) {
                    return new ResponseEntity<>(new ResponseNotification(HttpStatus.BAD_REQUEST.toString(),
                            MessageResponse.TipMessage.SAVED),
                            HttpStatus.BAD_REQUEST);
                }
                if (savedList.get(i).getStatus().equals(Constants.STATUS_DELETE)) {
                    savedList.get(i).setStatus(Constants.STATUS_SAVED);
                    savedList.get(i).setSaveDate(new Date());
                    savedRepository.save(savedList.get(i));
                    return new ResponseEntity<>(HttpStatus.OK);
                }
            }
        }
        Saved saved = new Saved();
        if ("post".equalsIgnoreCase(type)) {
            saved.setType("Post");
            saved.setUserCreate(postOptional.get().getUser().getFullName());
            saved.setImagePost(postOptional.get().getImage());
            saved.setContent(postOptional.get().getContent());
        }
        if ("groupPost".equalsIgnoreCase(type)) {
            saved.setType("Group post");
            saved.setGroupName(groupPost.get().getTheGroup().getGroupName());
            saved.setUserCreate(groupPost.get().getCreateBy());
            saved.setImagePost(groupPost.get().getImage());
            saved.setContent(groupPost.get().getContent());
        }
        saved.setIdPost(idPost);
        saved.setSaveDate(new Date());
        saved.setStatus(Constants.STATUS_SAVED);
        saved.setIdUser(idUser);
        savedRepository.save(saved);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Xóa bài viết đã lưu trữ
    @GetMapping("/removeSavePost")
    public ResponseEntity<?> removeSavePost(@RequestParam Long idPost, @RequestParam Long idSaved) {
        Optional<Post2> postOptional = postService.findById(idPost);
        if (!postOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_POST, idPost), HttpStatus.NOT_FOUND);
        }
        Optional<Saved> savedOptional = savedRepository.findById(idSaved);
        if (!savedOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_SAVE, idSaved),
                    HttpStatus.NOT_FOUND);
        }
        if (savedOptional.get().getIdPost().equals(postOptional.get().getId())) {
            savedOptional.get().setStatus(Constants.STATUS_DELETE);
            savedRepository.save(savedOptional.get());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponseNotification(HttpStatus.BAD_REQUEST.toString(),
                MessageResponse.ERROR), HttpStatus.BAD_REQUEST);
    }
}
