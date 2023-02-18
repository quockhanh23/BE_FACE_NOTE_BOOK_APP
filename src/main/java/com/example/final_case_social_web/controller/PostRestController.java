package com.example.final_case_social_web.controller;

import com.example.final_case_social_web.common.Common;
import com.example.final_case_social_web.common.Constants;
import com.example.final_case_social_web.common.Regex;
import com.example.final_case_social_web.dto.PostDTO;
import com.example.final_case_social_web.dto.UserDTO;
import com.example.final_case_social_web.model.*;
import com.example.final_case_social_web.notification.ResponseNotification;
import com.example.final_case_social_web.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@PropertySource("classpath:application.properties")
@CrossOrigin("*")
@RequestMapping("/api/posts")
@Slf4j
public class PostRestController {
    @Autowired
    private PostService postService;
    @Autowired
    private UserService userService;
    @Autowired
    private LikePostService likePostService;
    @Autowired
    private DisLikePostService disLikePostService;
    @Autowired
    private IconHeartService iconHeartService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ImageService imageService;

    @GetMapping("/allPostPublic")
    public ResponseEntity<?> reloadAllPostPublic(@RequestParam(required = false) Long idUser) {
        List<Post2> post2List;
        if (idUser != null) {
            post2List = postService.findAllPostByUser(idUser);
        } else {
            post2List = postService.allPost();
        }
        List<PostDTO> postDTOList = postService.filterListPost(post2List);
        return new ResponseEntity<>(postDTOList, HttpStatus.OK);
    }

    // Tạo mới bài viết
    @PostMapping("/createPost")
    public ResponseEntity<?> createPost(@RequestBody Post2 post, @RequestParam Long idUser) {
        if ((post.getContent().trim().equals(Constants.BLANK) || post.getContent() == null) && post.getImage() == null) {
            return new ResponseEntity<>(ResponseNotification.responseMessageDataField(Constants.DataField.CONTENT),
                    HttpStatus.BAD_REQUEST);
        }
        if (!Common.checkRegex(post.getContent(), Regex.CHECK_LENGTH_POST)) {
            return new ResponseEntity<>(ResponseNotification.responseMessageDataField(Constants.DataField.CONTENT),
                    HttpStatus.BAD_REQUEST);
        }
        if (post.getImage() != null) {
            Image image = imageService.createImageDefault(post.getImage(), post.getUser());
            imageService.save(image);
        }
        Optional<User> userOptional = userService.findById(idUser);
        if (!userOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_USER, idUser),
                    HttpStatus.NOT_FOUND);
        }
        postService.create(post);
        post.setUser(userOptional.get());
        postService.save(post);
        PostDTO postDTO = postService.mapper(post);
        postDTO.setUserDTO(userService.mapper(userOptional.get()));
        return new ResponseEntity<>(postDTO, HttpStatus.OK);
    }

    // Chỉnh sửa post
    @PutMapping("/updatePost")
    public ResponseEntity<?> update(@RequestParam Long idPost, @RequestParam Long idUser, @RequestBody Post2 post) {
        Optional<Post2> postOptional = postService.findById(idPost);
        if (!postOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_POST, idPost),
                    HttpStatus.NOT_FOUND);
        }
        Optional<User> userOptional = userService.findById(idUser);
        if (!userOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_USER, idUser),
                    HttpStatus.NOT_FOUND);
        }
        if (postOptional.get().getUser().getId().equals(userOptional.get().getId())) {
            postOptional.get().setEditAt(new Date());
            if (post.getContent() != null || post.getContent().trim().equals(Constants.BLANK)) {
                postOptional.get().setContent(post.getContent());
            }
            postService.save(postOptional.get());
            PostDTO postDTO = postService.mapper(postOptional.get());
            return new ResponseEntity<>(postDTO, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    // update like
    @DeleteMapping("/updateLikePost")
    public ResponseEntity<?> updateLikePost(@RequestParam Long idPost) {
        Optional<Post2> postOptional = postService.findById(idPost);
        if (!postOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_POST, idPost),
                    HttpStatus.NOT_FOUND);
        }
        List<LikePost> likePost = likePostService.findAllLikeByPostId(idPost);
        if (!CollectionUtils.isEmpty(likePost)) {
            postOptional.get().setNumberLike((long) likePost.size());
            postService.save(postOptional.get());
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // update disLike
    @DeleteMapping("/updateDisLikePost")
    public ResponseEntity<?> updateDisLikePost(@RequestParam Long idPost) {
        Optional<Post2> postOptional = postService.findById(idPost);
        if (!postOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_POST, idPost),
                    HttpStatus.NOT_FOUND);
        }
        List<DisLikePost> disLikePosts = disLikePostService.findAllDisLikeByPostId(idPost);
        if (!CollectionUtils.isEmpty(disLikePosts)) {
            postOptional.get().setNumberDisLike((long) disLikePosts.size());
            postService.save(postOptional.get());
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // update heart
    @DeleteMapping("/updateHeartPost")
    public ResponseEntity<?> updateHeartPost(@RequestParam Long idPost) {
        Optional<Post2> postOptional = postService.findById(idPost);
        if (!postOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_POST, idPost),
                    HttpStatus.NOT_FOUND);
        }
        List<IconHeart> iconHearts = iconHeartService.findAllHeartByPostId(idPost);
        if (!CollectionUtils.isEmpty(iconHearts)) {
            postOptional.get().setIconHeart((long) iconHearts.size());
            postService.save(postOptional.get());
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Đổi trạng thái post sang public
    @DeleteMapping("/changeStatusPublic")
    public ResponseEntity<?> changeStatusPublic(@RequestParam Long idPost, @RequestParam Long idUser) {
        if (!postService.checkPostPublic(idPost).isPresent()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_POST, idPost),
                    HttpStatus.NOT_FOUND);
        }
        Optional<User> userOptional = userService.findById(idUser);
        if (!userOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_USER, idUser),
                    HttpStatus.NOT_FOUND);
        }
        if (postService.checkPostPublic(idPost).get().getUser().getId().equals(userOptional.get().getId())) {
            postService.save(postService.checkPostPublic(idPost).get());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    // Đổi trạng thái post sang private
    @DeleteMapping("/changeStatusPrivate")
    public ResponseEntity<?> changeStatus(@RequestParam Long idPost,
                                          @RequestParam Long idUser) {
        if (!postService.checkPostPrivate(idPost).isPresent()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_POST, idPost),
                    HttpStatus.NOT_FOUND);
        }
        Optional<User> userOptional = userService.findById(idUser);
        if (!userOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_USER, idUser),
                    HttpStatus.NOT_FOUND);
        }
        if (postService.checkPostPrivate(idPost).get().getUser().getId().equals(userOptional.get().getId())) {
            postService.save(postService.checkPostPrivate(idPost).get());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    // Đổi trạng thái post sang delete (chuyển vào thùng rác)
    @DeleteMapping("/changeStatusDelete")
    public ResponseEntity<?> delete(@RequestParam Long idPost,
                                    @RequestParam Long idUser) {
        if (!postService.checkPostDelete(idPost).isPresent()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_POST, idPost),
                    HttpStatus.NOT_FOUND);
        }
        Optional<User> userOptional = userService.findById(idUser);
        if (!userOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_USER, idUser),
                    HttpStatus.NOT_FOUND);
        }
        if (postService.checkPostDelete(idPost).get().getUser().getId().equals(userOptional.get().getId())) {
            postService.save(postService.checkPostDelete(idPost).get());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    // Xoá hẳn post khỏi database
    @DeleteMapping("/deletePost")
    public ResponseEntity<?> deletePost(@RequestParam Long idPost,
                                        @RequestParam Long idUser) {
        Optional<Post2> postOptional = postService.findById(idPost);
        if (!postOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_POST, idPost),
                    HttpStatus.NOT_FOUND);
        }
        Optional<User> userOptional = userService.findById(idUser);
        if (!userOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_USER, idUser),
                    HttpStatus.NOT_FOUND);
        }
        if (postOptional.get().getStatus().equals(Constants.STATUS_DELETE)) {
            if (postOptional.get().isDelete()) {
                if (postOptional.get().getId().equals(userOptional.get().getId())) {
                    postService.delete(postOptional.get());
                    return new ResponseEntity<>(HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @GetMapping("findOnePostById")
    public ResponseEntity<?> findOnePostById(@RequestParam Long idPost) {
        Optional<Post2> postOptional = postService.findById(idPost);
        if (!postOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_POST, idPost),
                    HttpStatus.NOT_FOUND);
        }
        PostDTO postDTO = modelMapper.map(postOptional.get(), PostDTO.class);
        UserDTO userDTO = modelMapper.map(postOptional.get().getUser(), UserDTO.class);
        postDTO.setUserDTO(userDTO);
        return new ResponseEntity<>(postDTO, HttpStatus.OK);
    }

    // Tất cả bài viết trong thùng rác
    @GetMapping("/allPostInTrash")
    public ResponseEntity<?> allPostInTrash(@RequestParam Long idUser) {
        List<Post2> post2List = postService.findAllByUserIdAndDeleteTrue(idUser);
        List<PostDTO> postDTOList = postService.filterListPost(post2List);
        return new ResponseEntity<>(postDTOList, HttpStatus.OK);
    }
}
