package com.example.final_case_social_web.service.impl;

import com.example.final_case_social_web.common.Constants;
import com.example.final_case_social_web.dto.PostDTO;
import com.example.final_case_social_web.dto.UserDTO;
import com.example.final_case_social_web.model.*;
import com.example.final_case_social_web.repository.PostRepository;
import com.example.final_case_social_web.service.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private LikePostService likePostService;
    @Autowired
    private DisLikePostService disLikePostService;
    @Autowired
    private IconHeartService iconHeartService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private AnswerCommentService answerCommentService;
    @Autowired
    ModelMapper modelMapper;

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Iterable<Post2> findAll() {
        return postRepository.findAll();
    }

    @Override
    public Optional<Post2> findById(Long id) {
        return postRepository.findById(id);
    }

    @Override
    public Post2 save(Post2 post) {
        return postRepository.save(post);
    }

    @Override
    public List<Post2> findAllPostByUser(Long id) {
        return postRepository.findAllPostByUser(id);
    }

    @Override
    public List<Post2> allPost(Long id) {
        return postRepository.AllPost(id);
    }

    @Override
    public void delete(Post2 entity) {
        postRepository.delete(entity);
    }

    @Override
    public PostDTO mapper(Post2 post2) {
        PostDTO postDTO = modelMapper.map(post2, PostDTO.class);
        return postDTO;
    }

    @Override
    public void create(Post2 post2) {
        if (StringUtils.isEmpty(post2.getStatus())) {
            post2.setStatus(Constants.STATUS_PUBLIC);
        }
        post2.setEditAt(null);
        post2.setDelete(false);
        post2.setCreateAt(new Date());
        post2.setIconHeart(0L);
        post2.setNumberDisLike(0L);
        post2.setNumberLike(0L);
    }

    @Override
    public Post2 checkPost(Long idPost) {
        Optional<Post2> post2Optional = postRepository.findById(idPost);
        return post2Optional.orElse(null);
    }

    @Override
    public void saveAll(List<Post2> post2List) {
        postRepository.saveAll(post2List);
    }

    @Override
    public List<PostDTO> changeDTO(List<Post2> post2List) {
        List<PostDTO> postDTOList = new ArrayList<>();
        for (Post2 post2 : post2List) {
            UserDTO userDTO = modelMapper.map(post2.getUser(), UserDTO.class);
            PostDTO postDTO = modelMapper.map(post2, PostDTO.class);
            postDTO.setUserDTO(userDTO);
            postDTOList.add(postDTO);
        }
        return postDTOList;
    }

    @Override
    public List<Post2> findAllByUserIdAndDeleteTrue(Long user_id) {
        return postRepository.findAllByUserIdAndDeleteIsTrue(user_id);
    }

    @Override
    public List<PostDTO> filterListPost(List<Post2> post2List) {
        List<PostDTO> postDTOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(post2List)) {
            postDTOList = changeDTO(post2List);
            List<Long> listPostId = post2List.stream().map(Post2::getId).collect(Collectors.toList());
            List<LikePost> likePosts = likePostService.findAllByPostIdIn(listPostId);
            List<DisLikePost> disLikePosts = disLikePostService.findAllByPostIdIn(listPostId);
            List<IconHeart> iconHearts = iconHeartService.findAllByPostIdIn(listPostId);
            List<Comment> commentList = commentService.findAllByPostIdIn(listPostId);
            for (PostDTO postDTO : postDTOList) {
                Long idPost = postDTO.getId();
                List<LikePost> likePostList = likePosts.stream()
                        .filter(item -> item.getPost().getId().equals(idPost)).collect(Collectors.toList());
                List<DisLikePost> disLikePostList = disLikePosts.stream()
                        .filter(item -> item.getPost().getId().equals(idPost)).collect(Collectors.toList());
                List<IconHeart> iconHeartList = iconHearts.stream()
                        .filter(item -> item.getPost().getId().equals(idPost)).collect(Collectors.toList());
                List<Comment> comments = commentList.stream()
                        .filter(item -> item.getPost().getId().equals(idPost)).collect(Collectors.toList());
                List<Long> listCommentId = comments.stream().map(Comment::getId).collect(Collectors.toList());
                List<AnswerComment> answerCommentList = answerCommentService.findAllByCommentIdIn(listCommentId);
                int countAllCommentAndAnswerComment = listCommentId.size() + answerCommentList.size();
                postDTO.setNumberLike((long) likePostList.size());
                postDTO.setNumberDisLike((long) disLikePostList.size());
                postDTO.setIconHeart((long) iconHeartList.size());
                postDTO.setCountAllComment(countAllCommentAndAnswerComment);
            }
        }
        return postDTOList;
    }

//    public List<Post2> getListLCReFile(String lcRef, String requestCode, Integer productType) {
//        StringBuffer sql = new StringBuffer("Select * from tbl_ref_file where is_deleted = false ");
//        if (lcRef != null && !"".equals(lcRef)) {
//            sql.append(" and lc_ref =:lcRef ");
//        }
//        if (requestCode != null && !"".equals(requestCode)) {
//            sql.append(" and request_code = :requestCode ");
//        }
//        if (productType != null) {
//            sql.append(" and product_type = :productType ");
//        }
//
//        Query query = (Query) entityManager.createNativeQuery(sql.toString(), Comment.class);
//        if (lcRef != null && !"".equals(lcRef)) {
//            query.setParameter("lcRef", lcRef);
//        }
//        if (requestCode != null && !"".equals(requestCode)) {
//            query.setParameter("requestCode", requestCode);
//        }
//        if (productType != null) {
//            query.setParameter("productType", productType);
//        }
//        if (requestCode == null && productType == null) {
//            query.setParameter("lcRef", lcRef);
//        }
//        if (lcRef == null && productType == null) {
//            query.setParameter("requestCode", requestCode);
//        }
//        if (lcRef == null && requestCode == null) {
//            query.setParameter("productType", productType);
//        }
//        return query.getResultList();
//    }

}
