package com.example.final_case_social_web.controller;

import com.example.final_case_social_web.common.Common;
import com.example.final_case_social_web.common.Constants;
import com.example.final_case_social_web.common.MessageResponse;
import com.example.final_case_social_web.common.Regex;
import com.example.final_case_social_web.component.RedisBaseService;
import com.example.final_case_social_web.dto.PostDTO;
import com.example.final_case_social_web.dto.UserDTO;
import com.example.final_case_social_web.model.*;
import com.example.final_case_social_web.notification.ResponseNotification;
import com.example.final_case_social_web.repository.*;
import com.example.final_case_social_web.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

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
    @Autowired
    private HidePostRepository hidePostRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private LikePostRepository likePostRepository;
    @Autowired
    private DisLikePostRepository disLikePostRepository;
    @Autowired
    private IconHeartRepository iconHeartRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private RedisBaseService redisBaseService;

    @GetMapping("/allPostPublic")
    public ResponseEntity<?> allPostPublic(@RequestParam Long idUser,
                                           @RequestParam String type,
                                           @RequestParam(required = false) Long idUserVisit,
                                           @RequestHeader("Authorization") String authorization) {
        try {
            boolean check;
            if (idUserVisit != null) {
                check = userService.errorToken(authorization, idUserVisit);
            } else {
                check = userService.errorToken(authorization, idUser);
            }
            if (!check) {
                return new ResponseEntity<>(new ResponseNotification(HttpStatus.UNAUTHORIZED.toString(),
                        Constants.TOKEN, Constants.TOKEN + " " + MessageResponse.IN_VALID.toLowerCase()),
                        HttpStatus.UNAUTHORIZED);
            }
            List<Post2> post2List = postService.allPost(idUser);
            if (!CollectionUtils.isEmpty(post2List)) {
                if (type.equals("detailUser")) {
                    post2List = postService.findAllPostByUser(idUser);
                    List<PostDTO> postDTOList = postService.filterListPost(post2List);
                    return new ResponseEntity<>(postDTOList, HttpStatus.OK);
                }
                if (type.equals("getAll")) {
                    List<PostDTO> postDTOList = postService.filterListPost(post2List);
                    return new ResponseEntity<>(postDTOList, HttpStatus.OK);
                }
                if (type.equals("getAllByUser")) {
                    List<PostDTO> postDTOList = postService.filterListPost(post2List);
                    List<HidePost> hidePosts = hidePostRepository.findAllByIdUser(idUser);
                    if (!CollectionUtils.isEmpty(hidePosts)) {
                        List<Long> listIdPost = hidePosts.stream().map(HidePost::getIdPost).collect(Collectors.toList());
                        for (Long id : listIdPost) {
                            postDTOList.stream().filter(item -> item.getId().equals(id)).findFirst()
                                    .ifPresent(post2 -> post2.setContent(null));
                        }
                    }
                    return new ResponseEntity<>(postDTOList, HttpStatus.OK);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
    }

    @DeleteMapping("/hidePost")
    public ResponseEntity<?> hidePost(@RequestParam Long idUser, @RequestParam Long idPost,
                                      @RequestParam String type,
                                      @RequestHeader("Authorization") String authorization) {
        boolean check = userService.errorToken(authorization, idUser);
        if (!check) {
            return new ResponseEntity<>(new ResponseNotification(HttpStatus.UNAUTHORIZED.toString(),
                    Constants.TOKEN, Constants.TOKEN + " " + MessageResponse.IN_VALID.toLowerCase()),
                    HttpStatus.UNAUTHORIZED);
        }
        List<HidePost> list = hidePostRepository.findAllByIdUser(idUser);
        if (Constants.HIDE.equalsIgnoreCase(type)) {
            if (!CollectionUtils.isEmpty(list)) {
                for (HidePost post : list) {
                    if (post.getIdPost().equals(idPost) && post.getIdUser().equals(idUser)) {
                        return new ResponseEntity<>(HttpStatus.OK);
                    }
                }
            }
            HidePost hidePost = new HidePost();
            hidePost.setCreateAt(new Date());
            hidePost.setIdUser(idUser);
            hidePost.setIdPost(idPost);
            hidePostRepository.save(hidePost);
        }
        if (Constants.UN_HIDE.equalsIgnoreCase(type) && !CollectionUtils.isEmpty(list)) {
            for (HidePost post : list) {
                if (post.getIdPost().equals(idPost) && post.getIdUser().equals(idUser)) {
                    hidePostRepository.delete(post);
                    break;
                }
            }
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Tạo mới bài viết
    @PostMapping("/createPost")
    public ResponseEntity<?> createPost(@RequestBody Post2 post,
                                        @RequestParam Long idUser,
                                        @RequestHeader("Authorization") String authorization) {
        if ((post.getContent().trim().equals(Constants.BLANK)
                || (post.getContent() == null) && post.getImage() == null)
                || !Common.checkRegex(post.getContent(), Regex.CHECK_LENGTH_POST)) {
            return new ResponseEntity<>(ResponseNotification.responseMessageDataField(Constants.DataField.CONTENT),
                    HttpStatus.BAD_REQUEST);
        }
        ResponseEntity<?> responseEntity = Common.handlerWordsLanguage(post);
        if (null != responseEntity) return responseEntity;
        if (post.getImage() != null) {
            Image image = imageService.createImageDefault(post.getImage(), post.getUser());
            imageService.save(image);
        }
        Optional<User> userOptional = userService.findById(idUser);
        if (!userOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_USER, idUser),
                    HttpStatus.NOT_FOUND);
        }
        if (!userService.errorToken(authorization, idUser)) {
            return new ResponseEntity<>(new ResponseNotification(HttpStatus.UNAUTHORIZED.toString(),
                    Constants.TOKEN, Constants.TOKEN + " " + MessageResponse.IN_VALID.toLowerCase()),
                    HttpStatus.UNAUTHORIZED);
        }
        postService.create(post);
        post.setUser(userOptional.get());
        postService.save(post);
        redisBaseService.delete("post");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Chỉnh sửa post
    @PutMapping("/updatePost")
    public ResponseEntity<?> updatePost(@RequestParam Long idPost, @RequestParam Long idUser,
                                        @RequestBody Post2 post,
                                        @RequestHeader("Authorization") String authorization) {
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
        if (!userService.errorToken(authorization, idUser)) {
            return new ResponseEntity<>(new ResponseNotification(HttpStatus.UNAUTHORIZED.toString(),
                    Constants.TOKEN, Constants.TOKEN + " " + MessageResponse.IN_VALID.toLowerCase()),
                    HttpStatus.UNAUTHORIZED);
        }
        ResponseEntity<?> responseEntity = Common.handlerWordsLanguage(post);
        if (null != responseEntity) return responseEntity;
        if (postOptional.get().getUser().getId().equals(userOptional.get().getId())) {
            postOptional.get().setEditAt(new Date());
            if (post.getContent() != null || post.getContent().trim().equals(Constants.BLANK)) {
                postOptional.get().setContent(post.getContent());
            }
            postService.save(postOptional.get());
            redisBaseService.delete("post");
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponseNotification(HttpStatus.BAD_REQUEST.toString(),
                MessageResponse.IN_VALID, MessageResponse.DESCRIPTION),
                HttpStatus.BAD_REQUEST);
    }

    // Update like
    @DeleteMapping("/updateReflectPost")
    public ResponseEntity<?> updateReflectPost(@RequestParam Long idPost,
                                               @RequestParam String type) {
        Optional<Post2> postOptional = postService.findById(idPost);
        if (!postOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_POST, idPost),
                    HttpStatus.NOT_FOUND);
        }
        if ("like".equalsIgnoreCase(type)) {
            List<LikePost> likePost = likePostService.findAllLikeByPostId(idPost);
            if (!CollectionUtils.isEmpty(likePost)) {
                postOptional.get().setNumberLike((long) likePost.size());
            }
        }
        if ("disLike".equalsIgnoreCase(type)) {
            List<DisLikePost> disLikePosts = disLikePostService.findAllDisLikeByPostId(idPost);
            if (!CollectionUtils.isEmpty(disLikePosts)) {
                postOptional.get().setNumberDisLike((long) disLikePosts.size());
            }
        }
        if ("heart".equalsIgnoreCase(type)) {
            List<IconHeart> iconHearts = iconHeartService.findAllHeartByPostId(idPost);
            if (!CollectionUtils.isEmpty(iconHearts)) {
                postOptional.get().setIconHeart((long) iconHearts.size());
            }
        }
        if ("all".equalsIgnoreCase(type)) {
            List<LikePost> likePost = likePostService.findAllLikeByPostId(idPost);
            if (!CollectionUtils.isEmpty(likePost)) {
                postOptional.get().setNumberLike((long) likePost.size());
            }
            List<DisLikePost> disLikePosts = disLikePostService.findAllDisLikeByPostId(idPost);
            if (!CollectionUtils.isEmpty(disLikePosts)) {
                postOptional.get().setNumberDisLike((long) disLikePosts.size());
            }
            List<IconHeart> iconHearts = iconHeartService.findAllHeartByPostId(idPost);
            if (!CollectionUtils.isEmpty(iconHearts)) {
                postOptional.get().setIconHeart((long) iconHearts.size());
            }
        }
        postService.save(postOptional.get());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Đổi trạng thái post sang public, private, chuyển vào thùng rác
    @DeleteMapping("/changeStatusPost")
    public ResponseEntity<?> changeStatusPost(@RequestParam Long idPost,
                                              @RequestParam Long idUser,
                                              @RequestParam String type,
                                              @RequestHeader("Authorization") String authorization) {
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
        if (!userService.errorToken(authorization, idUser)) {
            return new ResponseEntity<>(new ResponseNotification(HttpStatus.UNAUTHORIZED.toString(),
                    Constants.TOKEN, Constants.TOKEN + " " + MessageResponse.IN_VALID.toLowerCase()),
                    HttpStatus.UNAUTHORIZED);
        }
        if ("Public".equals(type)) {
            postOptional.get().setStatus(Constants.STATUS_PUBLIC);
        }
        if ("Private".equals(type)) {
            postOptional.get().setStatus(Constants.STATUS_PRIVATE);
        }
        if ("Delete".equals(type)) {
            postOptional.get().setStatus(Constants.STATUS_DELETE);
            postOptional.get().setDelete(true);
        }
        if (!StringUtils.isEmpty(type) && ("Public".equals(type) || "Private".equals(type) || "Delete".equals(type))) {
            postService.save(postOptional.get());
            redisBaseService.delete("post");
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseNotification(HttpStatus.BAD_REQUEST.toString(),
                    MessageResponse.IN_VALID, MessageResponse.DESCRIPTION),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    @DeleteMapping("/actionAllPost")
    public ResponseEntity<?> actionAllPost(@RequestParam List<Long> listIdPost,
                                           @RequestParam Long idUser,
                                           @RequestParam String type,
                                           @RequestHeader("Authorization") String authorization) {
        Optional<User> userOptional = userService.findById(idUser);
        if (!userOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_USER, idUser),
                    HttpStatus.NOT_FOUND);
        }
        if (!userService.errorToken(authorization, idUser)) {
            return new ResponseEntity<>(new ResponseNotification(HttpStatus.UNAUTHORIZED.toString(),
                    Constants.TOKEN, Constants.TOKEN + " " + MessageResponse.IN_VALID.toLowerCase()),
                    HttpStatus.UNAUTHORIZED);
        }
        List<Post2> post2List = postRepository.findAllById(listIdPost);
        // Xóa tất cả bài viết
        if (Constants.DELETE_ALL.equalsIgnoreCase(type)) {
            List<Long> listIdPosts = post2List.stream().map(Post2::getId).collect(Collectors.toList());
            List<Comment> comments = commentRepository.findAllByPostIdInAndDeleteAtIsNull(listIdPosts);
            List<LikePost> likePosts = likePostService.findAllByPostIdIn(listIdPosts);
            List<DisLikePost> disLikePosts = disLikePostService.findAllByPostIdIn(listIdPosts);
            List<IconHeart> iconHearts = iconHeartService.findAllByPostIdIn(listIdPosts);
            postService.deleteRelateOfComment(comments);
            commentRepository.deleteInBatch(comments);
            likePostRepository.deleteInBatch(likePosts);
            disLikePostRepository.deleteInBatch(disLikePosts);
            iconHeartRepository.deleteInBatch(iconHearts);
            postRepository.deleteInBatch(post2List);
            redisBaseService.delete("post");
            return new ResponseEntity<>(HttpStatus.OK);
        }
        // Khôi phục tất cả
        if (Constants.RESTORE_ALL.equalsIgnoreCase(type)) {
            post2List.forEach(post2 -> {
                post2.setStatus(Constants.STATUS_PUBLIC);
                post2.setDelete(false);
            });
            postRepository.saveAll(post2List);
            redisBaseService.delete("post");
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponseNotification(HttpStatus.BAD_REQUEST.toString(),
                MessageResponse.IN_VALID, MessageResponse.DESCRIPTION),
                HttpStatus.BAD_REQUEST);
    }

    @GetMapping("findOnePostById")
    public ResponseEntity<?> findOnePostById(@RequestParam Long idPost,
                                             @RequestHeader("Authorization") String authorization) {
        Optional<Post2> postOptional = postService.findById(idPost);
        if (!postOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_POST, idPost),
                    HttpStatus.NOT_FOUND);
        }
        if (!userService.errorToken(authorization, postOptional.get().getUser().getId())) {
            return new ResponseEntity<>(new ResponseNotification(HttpStatus.UNAUTHORIZED.toString(),
                    Constants.TOKEN, Constants.TOKEN + " " + MessageResponse.IN_VALID.toLowerCase()),
                    HttpStatus.UNAUTHORIZED);
        }
        PostDTO postDTO = modelMapper.map(postOptional.get(), PostDTO.class);
        UserDTO userDTO = modelMapper.map(postOptional.get().getUser(), UserDTO.class);
        postDTO.setUserDTO(userDTO);
        return new ResponseEntity<>(postDTO, HttpStatus.OK);
    }

    // Tất cả bài viết trong thùng rác
    @GetMapping("/allPostInTrash")
    public ResponseEntity<?> allPostInTrash(@RequestParam Long idUser,
                                            @RequestHeader("Authorization") String authorization) {
        if (!userService.errorToken(authorization, idUser)) {
            return new ResponseEntity<>(new ResponseNotification(HttpStatus.UNAUTHORIZED.toString(),
                    Constants.TOKEN, Constants.TOKEN + " " + MessageResponse.IN_VALID.toLowerCase()),
                    HttpStatus.UNAUTHORIZED);
        }
        List<Post2> post2List = postService.findAllByUserIdAndDeleteTrue(idUser);
        List<PostDTO> postDTOList = postService.filterListPost(post2List);
        return new ResponseEntity<>(postDTOList, HttpStatus.OK);
    }
}
