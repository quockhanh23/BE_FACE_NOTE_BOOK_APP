package com.example.final_case_social_web.controller;

import com.example.final_case_social_web.common.Constants;
import com.example.final_case_social_web.common.MessageResponse;
import com.example.final_case_social_web.dto.PostDTO;
import com.example.final_case_social_web.dto.SavedDTO;
import com.example.final_case_social_web.dto.UserDTO;
import com.example.final_case_social_web.model.Post2;
import com.example.final_case_social_web.model.Saved;
import com.example.final_case_social_web.model.User;
import com.example.final_case_social_web.notification.ResponseNotification;
import com.example.final_case_social_web.service.PostService;
import com.example.final_case_social_web.service.SavedService;
import com.example.final_case_social_web.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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
    private SavedService savedService;
    @Autowired
    private UserService userService;
    @Autowired
    private PostService postService;
    @Autowired
    private ModelMapper modelMapper;

    // Danh sách đã lưu
    @GetMapping("/listSavedPost")
    public ResponseEntity<List<SavedDTO>> listSavedPost(@RequestParam Long idUser) {
        List<Saved> savedList = savedService.findAllSavedPost(idUser);
        List<SavedDTO> savedDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(savedList)) {
            for (Saved saved : savedList) {
                UserDTO userDTO = modelMapper.map(saved.getPost2().getUser(), UserDTO.class);
                PostDTO postDTO = modelMapper.map(saved.getPost2(), PostDTO.class);
                SavedDTO savedDTO = modelMapper.map(saved, SavedDTO.class);
                postDTO.setUserDTO(userDTO);
                savedDTO.setPostDTO(postDTO);
                savedDTOS.add(savedDTO);
            }
        }
        return new ResponseEntity<>(savedDTOS, HttpStatus.OK);
    }

    // Lưu trữ bài viết
    @GetMapping("/savePost")
    public ResponseEntity<?> savePost(@RequestParam Long idPost, @RequestParam Long idUser) {
        Optional<Post2> postOptional = postService.findById(idPost);
        if (!postOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_POST, idPost), HttpStatus.NOT_FOUND);
        }
        Optional<User> userOptional = userService.findById(idUser);
        if (!userOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_USER, idUser),
                    HttpStatus.NOT_FOUND);
        }
        Iterable<Saved> savedIterable = savedService.findAll();
        List<Saved> savedList = (List<Saved>) savedIterable;
        if (!CollectionUtils.isEmpty(savedList)) {
            for (Saved saved : savedList) {
                if (saved.getIdUser().equals(userOptional.get().getId())
                        && saved.getPost2().getId().equals(postOptional.get().getId())) {
                    if (saved.getStatus().equals(Constants.STATUS_SAVED)) {
                        return new ResponseEntity<>(new ResponseNotification(HttpStatus.BAD_REQUEST.toString(),
                                MessageResponse.TipMessage.SAVED),
                                HttpStatus.BAD_REQUEST);
                    }
                    if (saved.getStatus().equals(Constants.STATUS_DELETE)) {
                        saved.setStatus(Constants.STATUS_SAVED);
                        savedService.save(saved);
                        return new ResponseEntity<>(HttpStatus.OK);
                    }
                }
            }
        }
        Saved saved = new Saved();
        saved.setSaveDate(new Date());
        saved.setStatus(Constants.STATUS_SAVED);
        saved.setIdUser(idUser);
        saved.setPost2(postOptional.get());
        savedService.save(saved);
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
        Optional<Saved> savedOptional = savedService.findById(idSaved);
        if (!savedOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_SAVE, idSaved),
                    HttpStatus.NOT_FOUND);
        }
        if (savedOptional.get().getPost2().getId().equals(postOptional.get().getId())) {
            savedOptional.get().setStatus(Constants.STATUS_DELETE);
            savedService.save(savedOptional.get());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponseNotification(HttpStatus.BAD_REQUEST.toString(),
                MessageResponse.ERROR), HttpStatus.BAD_REQUEST);
    }
}
