package com.example.final_case_social_web.controller;

import com.example.final_case_social_web.common.Constants;
import com.example.final_case_social_web.common.MessageResponse;
import com.example.final_case_social_web.model.Image;
import com.example.final_case_social_web.model.User;
import com.example.final_case_social_web.notification.ResponseNotification;
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

    @GetMapping("/findAllImages")
    public ResponseEntity<?> findAllImages(@RequestParam Long idUser, @RequestParam String type) {
        List<Image> imageList = imageService.findAllImageByIdUser(idUser);
        if (CollectionUtils.isEmpty(imageList)) {
            imageList = new ArrayList<>();
        }
        if ("visit".equals(type)) {
            imageList.removeIf(item -> item.getStatus().equals("Private"));
        }
        return new ResponseEntity<>(imageList, HttpStatus.OK);
    }

    // Thêm ảnh
    @PostMapping("/addPhoto")
    public ResponseEntity<?> addPhoto(@RequestBody Image image,
                                      @RequestParam Long idUser,
                                      @RequestHeader("Authorization") String authorization) {
        if (userService.checkUser(idUser) == null) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_USER, idUser),
                    HttpStatus.NOT_FOUND);
        }
        Image imageDefault = imageService.createImageDefault(image.getLinkImage(), userService.checkUser(idUser));
        imageService.save(imageDefault);
        return new ResponseEntity<>(image, HttpStatus.OK);
    }

    @DeleteMapping("/privatePhoto")
    public ResponseEntity<?> privatePhoto(@RequestParam Long idUser,
                                          @RequestParam Long idImage,
                                          @RequestHeader("Authorization") String authorization) {
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
        if (imageOptional.get().getUser().getId().equals(userOptional.get().getId())) {
            imageOptional.get().setStatus(Constants.STATUS_PRIVATE);
            imageService.save(imageOptional.get());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponseNotification(HttpStatus.BAD_REQUEST.toString(),
                MessageResponse.NO_VALID, MessageResponse.DESCRIPTION),
                HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/publicPhoto")
    public ResponseEntity<?> publicPhoto(@RequestParam Long idUser,
                                         @RequestParam Long idImage,
                                         @RequestHeader("Authorization") String authorization) {
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
        if (imageOptional.get().getUser().getId().equals(userOptional.get().getId())) {
            imageOptional.get().setStatus(Constants.STATUS_PUBLIC);
            imageService.save(imageOptional.get());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponseNotification(HttpStatus.BAD_REQUEST.toString(),
                MessageResponse.NO_VALID, MessageResponse.DESCRIPTION),
                HttpStatus.BAD_REQUEST);
    }

    // Chuyển vào thùng rác
    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam Long idImage, @RequestParam Long idUser,
                                    @RequestHeader("Authorization") String authorization) {
        Optional<Image> imageOptional = imageService.findById(idImage);
        if (!imageOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_IMAGE, idImage),
                    HttpStatus.NOT_FOUND);
        }
        Optional<User> userOptional = userService.findById(idUser);
        if (!userOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_USER, idUser),
                    HttpStatus.NOT_FOUND);
        }
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
            return new ResponseEntity<>(imageOptional.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponseNotification(HttpStatus.BAD_REQUEST.toString(),
                MessageResponse.NO_VALID, MessageResponse.DESCRIPTION),
                HttpStatus.BAD_REQUEST);
    }

    // Khôi phục ảnh
    @DeleteMapping("/restoreImage")
    public ResponseEntity<?> restoreImage(@RequestParam Long idImage, @RequestParam Long idUser,
                                          @RequestHeader("Authorization") String authorization) {
        Optional<Image> imageOptional = imageService.findById(idImage);
        if (!imageOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_IMAGE, idImage),
                    HttpStatus.NOT_FOUND);
        }
        Optional<User> userOptional = userService.findById(idUser);
        if (!userOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_USER, idUser),
                    HttpStatus.NOT_FOUND);
        }
        if (imageOptional.get().getUser().getId().equals(userOptional.get().getId())) {
            imageOptional.get().setStatus(Constants.STATUS_PUBLIC);
            imageOptional.get().setDeleteAt(null);
            imageService.save(imageOptional.get());
        }
        return new ResponseEntity<>(new ResponseNotification(HttpStatus.BAD_REQUEST.toString(),
                MessageResponse.NO_VALID, MessageResponse.DESCRIPTION),
                HttpStatus.BAD_REQUEST);
    }

    // Xóa hẳn ảnh
    @DeleteMapping("/deleteImage")
    public ResponseEntity<?> deleteImage(@RequestParam Long idImage, @RequestParam Long idUser,
                                         @RequestHeader("Authorization") String authorization) {
        Optional<Image> imageOptional = imageService.findById(idImage);
        if (!imageOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_IMAGE, idImage),
                    HttpStatus.NOT_FOUND);
        }
        Optional<User> userOptional = userService.findById(idUser);
        if (!userOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_USER, idUser),
                    HttpStatus.NOT_FOUND);
        }
        if (imageOptional.get().getUser().getId().equals(userOptional.get().getId())) {
            imageService.delete(imageOptional.get());
            return new ResponseEntity<>(imageOptional.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponseNotification(HttpStatus.BAD_REQUEST.toString(),
                MessageResponse.NO_VALID, MessageResponse.DESCRIPTION),
                HttpStatus.BAD_REQUEST);
    }

    // Danh sách ảnh đã xóa
    @GetMapping("/getAllImageDeleted")
    public ResponseEntity<?> getAllImageDeleted(@RequestParam Long idUser,
                                                @RequestHeader("Authorization") String authorization) {
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
}
