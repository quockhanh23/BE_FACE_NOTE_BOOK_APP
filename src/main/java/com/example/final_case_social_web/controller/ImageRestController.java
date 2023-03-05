package com.example.final_case_social_web.controller;

import com.example.final_case_social_web.common.Constants;
import com.example.final_case_social_web.common.MessageResponse;
import com.example.final_case_social_web.model.Image;
import com.example.final_case_social_web.model.User;
import com.example.final_case_social_web.notification.ResponseNotification;
import com.example.final_case_social_web.repository.ImageRepository;
import com.example.final_case_social_web.service.ImageService;
import com.example.final_case_social_web.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@PropertySource("classpath:application.properties")
@CrossOrigin("*")
@RequestMapping("/api/images")
@Slf4j
public class ImageRestController {
    @Autowired
    private ImageService imageService;
    @Autowired
    private UserService userService;
    @Autowired
    private ImageRepository imageRepository;

    @GetMapping("/findAllImages")
    public ResponseEntity<?> findAllImages(@RequestParam Long idUser,
                                           @RequestParam String type,
                                           @RequestHeader("Authorization") String authorization) {
        userService.checkToken(authorization, idUser);
        List<Image> imageList = imageService.findAllImageByIdUser(idUser);
        if (CollectionUtils.isEmpty(imageList)) {
            imageList = new ArrayList<>();
        }
        if ("visit".equals(type)) {
            imageList.removeIf(item -> item.getStatus().equals("Private"));
        }
        return new ResponseEntity<>(imageList, HttpStatus.OK);
    }

    @GetMapping("/findById")
    public ResponseEntity<?> findById(@RequestParam Long idImage) {
        Optional<Image> imageOptional = imageService.findById(idImage);
        if (!imageOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(imageOptional.get(), HttpStatus.OK);
    }

    // Thêm ảnh
    @PostMapping("/addPhoto")
    public ResponseEntity<?> addPhoto(@RequestBody Image image,
                                      @RequestParam Long idUser,
                                      @RequestHeader("Authorization") String authorization) {
        userService.checkToken(authorization, idUser);
        if (userService.checkUser(idUser) == null) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_USER, idUser),
                    HttpStatus.NOT_FOUND);
        }
        Image imageDefault = imageService.createImageDefault(image.getLinkImage(), userService.checkUser(idUser));
        imageService.save(imageDefault);
        return new ResponseEntity<>(image, HttpStatus.OK);
    }

    @DeleteMapping("/actionPhoto")
    public ResponseEntity<?> actionPhoto(@RequestParam Long idUser,
                                         @RequestParam Long idImage,
                                         @RequestParam String type,
                                         @RequestHeader("Authorization") String authorization) {
        userService.checkToken(authorization, idUser);
        Optional<User> userOptional = userService.findById(idUser);
        if (!userOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_USER, idUser),
                    HttpStatus.NOT_FOUND);
        }
        Optional<Image> imageOptional = imageService.findById(idImage);
        if (!imageOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_IMAGE, idImage),
                    HttpStatus.NOT_FOUND);
        }
        if ("Private".equals(type)) {
            if (imageOptional.get().getUser().getId().equals(userOptional.get().getId())) {
                imageOptional.get().setStatus(Constants.STATUS_PRIVATE);
                imageService.save(imageOptional.get());
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        if ("Public".equals(type)) {
            if (imageOptional.get().getUser().getId().equals(userOptional.get().getId())) {
                imageOptional.get().setStatus(Constants.STATUS_PUBLIC);
                imageService.save(imageOptional.get());
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        if ("Delete".equals(type)) {
            if (imageOptional.get().getUser().getId().equals(userOptional.get().getId())) {
                imageOptional.get().setStatus(Constants.STATUS_DELETE);
                imageOptional.get().setDeleteAt(new Date());
                imageService.save(imageOptional.get());
                if (userOptional.get().getCover().equals(imageOptional.get().getLinkImage())) {
                    userOptional.get().setCover(Constants.ImageDefault.DEFAULT_BACKGROUND_2);
                    userService.save(userOptional.get());
                }
                if (userOptional.get().getAvatar().equals(imageOptional.get().getLinkImage())) {
                    if (userOptional.get().getGender().equals(Constants.GENDER_FEMALE)) {
                        userOptional.get().setAvatar(Constants.ImageDefault.DEFAULT_IMAGE_AVATAR_FEMALE);
                    } else if (userOptional.get().getGender().equals(Constants.GENDER_DEFAULT)) {
                        userOptional.get().setAvatar(Constants.ImageDefault.DEFAULT_IMAGE_AVATAR_LGBT);
                    } else {
                        userOptional.get().setAvatar(Constants.ImageDefault.DEFAULT_IMAGE_AVATAR_MALE);
                    }
                    userService.save(userOptional.get());
                }
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        if ("Restore".equals(type)) {
            if (imageOptional.get().getUser().getId().equals(userOptional.get().getId())) {
                imageOptional.get().setStatus(Constants.STATUS_PUBLIC);
                imageOptional.get().setDeleteAt(null);
                imageService.save(imageOptional.get());
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        if ("DeleteDB".equals(type)) {
            if (imageOptional.get().getUser().getId().equals(userOptional.get().getId())) {
                imageService.delete(imageOptional.get());
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(new ResponseNotification(HttpStatus.BAD_REQUEST.toString(),
                MessageResponse.NO_VALID, MessageResponse.DESCRIPTION),
                HttpStatus.BAD_REQUEST);
    }

    // Danh sách ảnh đã xóa
    @GetMapping("/getAllImageDeleted")
    public ResponseEntity<?> getAllImageDeleted(@RequestParam Long idUser,
                                                @RequestHeader("Authorization") String authorization) {
        userService.checkToken(authorization, idUser);
        Optional<User> userOptional = userService.findById(idUser);
        if (!userOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_USER, idUser),
                    HttpStatus.NOT_FOUND);
        }
        List<Image> imageListDeleted = imageService.findAllImageDeletedByUserId(idUser);
        if (CollectionUtils.isEmpty(imageListDeleted)) {
            imageListDeleted = new ArrayList<>();
        }
        return new ResponseEntity<>(imageListDeleted, HttpStatus.OK);
    }

    // Xóa tất cả ảnh trong thùng rác
    @DeleteMapping("/actionAllImage")
    public ResponseEntity<?> deleteAllImage(@RequestParam Long idUser,
                                            @RequestParam String type,
                                            @RequestHeader("Authorization") String authorization) {
        userService.checkToken(authorization, idUser);
        Optional<User> userOptional = userService.findById(idUser);
        if (!userOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_USER, idUser),
                    HttpStatus.NOT_FOUND);
        }
        List<Image> imageListDeleted = imageService.findAllImageDeletedByUserId(idUser);
        if (!CollectionUtils.isEmpty(imageListDeleted)) {
            if (Constants.DELETE_ALL.equalsIgnoreCase(type)) {
                imageRepository.deleteInBatch(imageListDeleted);
                return new ResponseEntity<>(HttpStatus.OK);
            }
            if (Constants.RESTORE_ALL.equalsIgnoreCase(type)) {
                imageListDeleted.forEach(image -> {
                    image.setStatus(Constants.STATUS_PUBLIC);
                    image.setDeleteAt(null);
                });
                imageRepository.saveAll(imageListDeleted);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(new ResponseNotification(HttpStatus.BAD_REQUEST.toString(),
                MessageResponse.NO_VALID, MessageResponse.DESCRIPTION),
                HttpStatus.BAD_REQUEST);
    }
}
