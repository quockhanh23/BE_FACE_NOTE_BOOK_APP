package com.example.final_case_social_web.controller;

import com.example.final_case_social_web.common.Constants;
import com.example.final_case_social_web.model.*;
import com.example.final_case_social_web.notification.ResponseNotification;
import com.example.final_case_social_web.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@RestController
@PropertySource("classpath:application.properties")
@CrossOrigin("*")
@RequestMapping("/api/refs")
@Slf4j
public class ReflectRestController {
    @Autowired
    private LikePostService likePostService;
    @Autowired
    private DisLikePostService disLikePostService;
    @Autowired
    private UserService userService;
    @Autowired
    private PostService postService;
    @Autowired
    private LikeCommentService likeCommentService;
    @Autowired
    private DisLikeCommentService disLikeCommentService;
    @Autowired
    private IconHeartService iconHeartService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private NotificationService notificationService;

    // Xem like của post
    @GetMapping("/getAllLike")
    public ResponseEntity<?> getAllLike(@RequestParam Long idPost) {
        Optional<Post2> postOptional = postService.findById(idPost);
        if (!postOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_POST, idPost),
                    HttpStatus.NOT_FOUND);
        }
        List<LikePost> likePosts = likePostService.findAllLikeByPostId(idPost);
        if (CollectionUtils.isEmpty(likePosts)) {
            likePosts = new ArrayList<>();
        }
        return new ResponseEntity<>(likePosts, HttpStatus.OK);
    }

    @GetMapping("/getAllHeart")
    public ResponseEntity<?> getAllHeart(@RequestParam Long idPost) {
        Optional<Post2> postOptional = postService.findById(idPost);
        if (!postOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_POST, idPost),
                    HttpStatus.NOT_FOUND);
        }
        List<IconHeart> iconHearts = iconHeartService.findAllHeartByPostId(idPost);
        if (CollectionUtils.isEmpty(iconHearts)) {
            iconHearts = new ArrayList<>();
        }
        return new ResponseEntity<>(iconHearts, HttpStatus.OK);
    }

    // Xem dislike của post
    @GetMapping("/getAllDisLike")
    public ResponseEntity<?> getAllDisLike(@RequestParam Long idPost) {
        Optional<Post2> postOptional = postService.findById(idPost);
        if (!postOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_POST, idPost),
                    HttpStatus.NOT_FOUND);
        }
        List<DisLikePost> disLikePosts = disLikePostService.findAllDisLikeByPostId(idPost);
        if (CollectionUtils.isEmpty(disLikePosts)) {
            disLikePosts = new ArrayList<>();
        }
        return new ResponseEntity<>(disLikePosts, HttpStatus.OK);
    }

    // Tạo, xóa like
    @PostMapping("/createLike")
    public ResponseEntity<?> createLike(@RequestBody LikePost likePost,
                                        @RequestParam Long idPost,
                                        @RequestParam Long idUser) {
        List<LikePost> likePostIterable = likePostService.findLike(idPost, idUser);
        if (likePostIterable.size() == 1) {
            likePostService.delete(likePostIterable.get(0));
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        Optional<User> userOptional = userService.findById(idUser);
        if (!userOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_USER, idUser), HttpStatus.NOT_FOUND);
        }
        Optional<Post2> postOptional = postService.findById(idPost);
        if (!postOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_POST, idPost),
                    HttpStatus.NOT_FOUND);
        }
        likePost.setUserLike(userService.checkUser(idUser));
        likePost.setCreateAt(LocalDateTime.now());
        likePost.setPost(postService.checkPost(idPost));
        likePostService.save(likePost);
        if (!postOptional.get().getUser().getId().equals(idUser)) {
            String title = Constants.Notification.TITLE_LIKE_POST;
            String type = Constants.Notification.TYPE_POST;
            Notification notification = notificationService.
                    createDefault(postOptional.get().getUser(), userOptional.get(), title, idPost, type);
            notificationService.save(notification);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Tạo, xóa dislike
    @PostMapping("/createDisLike")
    public ResponseEntity<?> createDisLike(@RequestBody DisLikePost disLikePost,
                                           @RequestParam Long idPost,
                                           @RequestParam Long idUser) {
        List<DisLikePost> disLikePosts = disLikePostService.findDisLike(idPost, idUser);
        if (disLikePosts.size() == 1) {
            disLikePosts.get(0).setUserDisLike(null);
            disLikePostService.save(disLikePosts.get(0));
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        Optional<User> userOptional = userService.findById(idUser);
        if (!userOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_USER, idUser), HttpStatus.NOT_FOUND);
        }
        Optional<Post2> postOptional = postService.findById(idPost);
        if (!postOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_POST, idPost),
                    HttpStatus.NOT_FOUND);
        }
        disLikePost.setUserDisLike(userService.checkUser(idUser));
        disLikePost.setCreateAt(new Date());
        disLikePost.setPost(postService.checkPost(idPost));
        disLikePostService.save(disLikePost);
        if (!postOptional.get().getUser().getId().equals(idUser)) {
            String title = Constants.Notification.TITLE_DISLIKE_POST;
            String type = Constants.Notification.TYPE_POST;
            Notification notification = notificationService.
                    createDefault(postOptional.get().getUser(), userOptional.get(), title, idPost, type);
            notificationService.save(notification);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Tạo, xóa heart
    @PostMapping("/createHeart")
    public ResponseEntity<?> createHeart(@RequestBody IconHeart iconHeart,
                                         @RequestParam Long idPost,
                                         @RequestParam Long idUser) {
        List<IconHeart> iconHearts = iconHeartService.findHeart(idPost, idUser);
        if (iconHearts.size() == 1) {
            iconHeartService.delete(iconHearts.get(0));
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        Optional<User> userOptional = userService.findById(idUser);
        if (!userOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_USER, idUser), HttpStatus.NOT_FOUND);
        }
        Optional<Post2> postOptional = postService.findById(idPost);
        if (!postOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_POST, idPost),
                    HttpStatus.NOT_FOUND);
        }
        iconHeart.setUser(userOptional.get());
        iconHeart.setCreateAt(new Date());
        iconHeart.setPost(postService.checkPost(idPost));
        iconHeartService.save(iconHeart);
        if (!postOptional.get().getUser().getId().equals(idUser)) {
            String title = Constants.Notification.TITLE_HEART_POST;
            String type = Constants.Notification.TYPE_POST;
            Notification notification = notificationService.
                    createDefault(postOptional.get().getUser(), userOptional.get(), title, idPost, type);
            notificationService.save(notification);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Tạo, xóa like comment
    @PostMapping("/createLikeComment")
    public ResponseEntity<?> createLikeComment(@RequestBody LikeComment likeComment,
                                               @RequestParam Long idComment,
                                               @RequestParam Long idUser) {
        List<LikeComment> likeComments = likeCommentService.findLikeComment(idComment, idUser);
        if (likeComments.size() == 1) {
            likeCommentService.delete(likeComments.get(0));
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        Optional<User> userOptional = userService.findById(idUser);
        if (!userOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_USER, idUser), HttpStatus.NOT_FOUND);
        }
        Optional<Comment> commentOptional = commentService.findById(idComment);
        if (!commentOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_COMMENT, idComment),
                    HttpStatus.NOT_FOUND);
        }
        likeComment.setUserLike(userService.checkUser(idUser));
        likeComment.setCreateAt(new Date());
        likeComment.setComment(commentOptional.get());
        likeCommentService.save(likeComment);
        if (!commentOptional.get().getUser().getId().equals(idUser)) {
            String title = Constants.Notification.TITLE_LIKE_COMMENT;
            String type = Constants.Notification.TYPE_COMMENT;
            Notification notification = notificationService.
                    createDefault(commentOptional.get().getUser(), userOptional.get(), title, idComment, type);
            notificationService.save(notification);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Tạo, xóa dislike comment
    @PostMapping("/createDisLikeComment")
    public ResponseEntity<?> createDisLikeComment(@RequestBody DisLikeComment disLikeComment,
                                                  @RequestParam Long idComment,
                                                  @RequestParam Long idUser) {
        List<DisLikeComment> disLikeComments = disLikeCommentService.findDisLikeComment(idComment, idUser);
        if (disLikeComments.size() == 1) {
            disLikeCommentService.delete(disLikeComments.get(0));
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        Optional<User> userOptional = userService.findById(idUser);
        if (!userOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_USER, idUser), HttpStatus.NOT_FOUND);
        }
        Optional<Comment> commentOptional = commentService.findById(idComment);
        if (!commentOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_COMMENT, idComment),
                    HttpStatus.NOT_FOUND);
        }
        disLikeComment.setUserDisLike(userService.checkUser(idUser));
        disLikeComment.setCreateAt(new Date());
        disLikeComment.setComment(commentOptional.get());
        disLikeCommentService.save(disLikeComment);
        if (!commentOptional.get().getUser().getId().equals(idUser)) {
            String title = Constants.Notification.TITLE_DISLIKE_COMMENT;
            String type = Constants.Notification.TYPE_COMMENT;
            Notification notification = notificationService.
                    createDefault(commentOptional.get().getUser(), userOptional.get(), title, idComment, type);
            notificationService.save(notification);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
