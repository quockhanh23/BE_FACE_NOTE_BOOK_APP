package com.example.final_case_social_web.controller;

import com.example.final_case_social_web.common.Common;
import com.example.final_case_social_web.common.Constants;
import com.example.final_case_social_web.common.MessageResponse;
import com.example.final_case_social_web.common.Regex;
import com.example.final_case_social_web.dto.*;
import com.example.final_case_social_web.model.*;
import com.example.final_case_social_web.notification.ResponseNotification;
import com.example.final_case_social_web.repository.LastUserLoginRepository;
import com.example.final_case_social_web.repository.VerificationTokenRepository;
import com.example.final_case_social_web.service.EmailService;
import com.example.final_case_social_web.service.ImageService;
import com.example.final_case_social_web.service.RoleService;
import com.example.final_case_social_web.service.UserService;
import com.example.final_case_social_web.service.impl.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@PropertySource("classpath:application.properties")
@CrossOrigin("*")
@RequestMapping("/api")
@Slf4j
public class UserController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private LastUserLoginRepository lastUserLoginRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ImageService imageService;
    @Autowired
    private EmailService emailService;

    @GetMapping("/saveHistoryLogin")
    public ResponseEntity<?> historyLogin(@RequestParam Long idUserLogin, @RequestHeader("IP") String IP) {
        Optional<User> userOptional = userService.findById(idUserLogin);
        if (!userOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_USER, idUserLogin), HttpStatus.NOT_FOUND);
        }
        Optional<LastUserLogin> userLoginOptional = lastUserLoginRepository.findAllByIdUser(userOptional.get().getId());
        if (userLoginOptional.isPresent()) {
            userLoginOptional.get().setIdUser(userOptional.get().getId());
            userLoginOptional.get().setLoginTime(new Date());
            userLoginOptional.get().setUserName(userOptional.get().getUsername());
            userLoginOptional.get().setAvatar(userOptional.get().getAvatar());
            userLoginOptional.get().setFullName(userOptional.get().getFullName());
            userLoginOptional.get().setIpAddress(IP);
            lastUserLoginRepository.save(userLoginOptional.get());
        } else {
            LastUserLogin userLogin = new LastUserLogin();
            userLogin.setIdUser(userOptional.get().getId());
            userLogin.setUserName(userOptional.get().getUsername());
            userLogin.setLoginTime(new Date());
            userLogin.setAvatar(userOptional.get().getAvatar());
            userLogin.setFullName(userOptional.get().getFullName());
            userLogin.setIpAddress(IP);
            lastUserLoginRepository.save(userLogin);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Lịch sử đăng nhập local
    @GetMapping("/historyLogin")
    public ResponseEntity<?> saveHistoryLogin() {
        List<LastUserLogin> userLogins = lastUserLoginRepository.historyLogin();
        if (CollectionUtils.isEmpty(userLogins)) {
            userLogins = new ArrayList<>();
        }
        return new ResponseEntity<>(userLogins, HttpStatus.OK);
    }

    @GetMapping("/searchByFullNameOrEmail")
    public ResponseEntity<?> searchByFullNameOrEmail(String search, @RequestParam Long idUserLogin) {
        List<UserBlackListDTO> list = new ArrayList<>();
        if (!StringUtils.isEmpty(search)) {
            List<User> userList = userService.findAllByEmailAndFullName(search, idUserLogin);
            if (!CollectionUtils.isEmpty(userList)) {
                userList.forEach(user -> {
                    UserBlackListDTO userBlackListDTO = new UserBlackListDTO();
                    BeanUtils.copyProperties(user, userBlackListDTO);
                    list.add(userBlackListDTO);
                });
            }
        }
        list.removeIf(item -> item.getId().equals(idUserLogin));
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/searchAll")
    public ResponseEntity<?> searchAll(String search, @RequestParam Long idUserLogin) {
        List<UserSearchDTO> list = new ArrayList<>();
        List<User> userList = userService.searchAll(search, idUserLogin);
        if (!CollectionUtils.isEmpty(userList)) {
            userList.forEach(user -> {
                UserSearchDTO userDTO = new UserSearchDTO();
                BeanUtils.copyProperties(user, userDTO);
                list.add(userDTO);
            });
        }
        list.removeIf(item -> item.getId().equals(idUserLogin));
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    // Tìm kiếm bạn bè của người dùng
    @GetMapping("/searchFriend")
    public ResponseEntity<?> searchFriend(@RequestParam String search, @RequestParam Long idUser) {
        List<User> listFriend = userService.allFriendByUserId(idUser);
        List<UserDTO> userDTOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(listFriend)) {
            listFriend.forEach(user -> {
                UserDTO userDTO = new UserDTO();
                BeanUtils.copyProperties(user, userDTO);
                userDTOList.add(userDTO);
            });
            for (int i = 0; i < listFriend.size(); i++) {
                List<User> friendOfFriend = userService.allFriendByUserId(userDTOList.get(i).getId());
                List<User> mutualFriends = new ArrayList<>();
                if (!CollectionUtils.isEmpty(friendOfFriend)) {
                    for (UserDTO userDTO : userDTOList) {
                        for (User user : friendOfFriend) {
                            if (userDTO.getId().equals(user.getId())) {
                                mutualFriends.add(user);
                            }
                        }
                    }
                }
                userDTOList.get(i).setMutualFriends(mutualFriends.size());
            }
        }
        List<User> friend = userService.searchFriend(search, idUser);
        List<UserDTO> list = new ArrayList<>();
        List<Long> idList = friend.stream().map(User::getId).collect(Collectors.toList());
        for (Long id : idList) {
            userDTOList.stream().filter(item -> item.getId().equals(id)).findFirst().ifPresent(list::add);
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    // Đăng kí tài khoản
    @Transactional
    @PostMapping("/register")
    public ResponseEntity<?> createUser(@Valid @RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (!Common.checkRegex(user.getUsername(), Regex.CHECK_USER_NAME)) {
            return new ResponseEntity<>(new ResponseNotification(HttpStatus.BAD_REQUEST.toString(),
                    MessageResponse.RegisterMessage.NO_VALID_USER_NAME),
                    HttpStatus.BAD_REQUEST);
        }
        if (!userService.isCorrectConfirmPassword(user)) {
            return new ResponseEntity<>(new ResponseNotification(HttpStatus.BAD_REQUEST.toString(),
                    MessageResponse.RegisterMessage.WRONG_CONFIRM_PASSWORD),
                    HttpStatus.BAD_REQUEST);
        }
        ResponseNotification responseNotification = userService.checkExistUserNameAndEmail(user);
        if (responseNotification != null) {
            return new ResponseEntity<>(responseNotification, HttpStatus.BAD_REQUEST);
        }
        Set<Role> roles = new HashSet<>();
        Role role;
        if (user.getRoles() != null) {
            role = roleService.findByName(Constants.Roles.ROLE_ADMIN);
        } else {
            role = roleService.findByName(Constants.Roles.ROLE_USER);
        }
        roles.add(role);
        user.setRoles(roles);
        userService.createDefault(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setConfirmPassword(passwordEncoder.encode(user.getConfirmPassword()));
        user.setStatus(Constants.STATUS_ACTIVE);
        userService.save(user);

        if ((user.getAvatar().equals(Constants.ImageDefault.DEFAULT_IMAGE_AVATAR_MALE))
                || (user.getAvatar().equals(Constants.ImageDefault.DEFAULT_IMAGE_AVATAR_LGBT))
                || (user.getAvatar().equals(Constants.ImageDefault.DEFAULT_IMAGE_AVATAR_FEMALE))) {
        } else {
            Image image = imageService.createImageDefault(user.getAvatar(), user);
            imageService.save(image);
        }
        if ((user.getCover().equals(Constants.ImageDefault.DEFAULT_BACKGROUND))
                || (user.getCover().equals(Constants.ImageDefault.DEFAULT_BACKGROUND_2))
                || (user.getCover().equals(Constants.ImageDefault.DEFAULT_BACKGROUND_3))) {
        } else {
            Image image = imageService.createImageDefault(user.getCover(), user);
            imageService.save(image);
        }
//        emailService.sendMail(user.getEmail(), MessageResponse.Email.WELL_COME + user.getFullName(), MessageResponse.Email.THANK);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    // Lấy lại mật khẩu
    @Transactional
    @PostMapping("/passwordRetrieval")
    public ResponseEntity<?> passwordRetrieval(@RequestBody PasswordRetrieval passwordRetrieval,
                                               @RequestHeader("Authorization") String authorization) {
        if (passwordRetrieval == null) {
            return new ResponseEntity<>(new ResponseNotification(HttpStatus.BAD_REQUEST.toString(),
                    MessageResponse.NO_VALID), HttpStatus.BAD_REQUEST);
        }
        try {
            Optional<User> userOptional = userService
                    .findUserByEmailAndUserName(passwordRetrieval.getUserName(), passwordRetrieval.getEmail());
            if (!userOptional.isPresent()) {
                return new ResponseEntity<>(new ResponseNotification(HttpStatus.NOT_FOUND.toString(),
                        MessageResponse.RegisterMessage.PASSWORD_RETRIEVAL_FAIL),
                        HttpStatus.NOT_FOUND);
            }
            userService.checkToken(authorization, userOptional.get().getId());
            String newPassword = RandomStringUtils.randomAscii(6);
            userOptional.get().setPassword(passwordEncoder.encode(newPassword));
            userService.save(userOptional.get());
            emailService.sendMail(userOptional.get().getEmail(),
                    MessageResponse.Email.RESET_PASSWORD + MessageResponse.Email.SPACE + MessageResponse.Email.APP,
                    MessageResponse.Email.NEW_PASSWORD + newPassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ResponseNotification(HttpStatus.OK.toString(),
                MessageResponse.RegisterMessage.PASSWORD_RETRIEVAL + passwordRetrieval.getEmail()),
                HttpStatus.OK);
    }

    // Đổi mật khẩu
    @Transactional
    @PostMapping("/matchPassword")
    public ResponseEntity<?> matches(@RequestBody UserChangePassword userChangePassword,
                                     @RequestParam Long idUser,
                                     @RequestHeader("Authorization") String authorization) {
        Optional<User> userOptional = userService.findById(idUser);
        if (!userOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_USER, idUser), HttpStatus.NOT_FOUND);
        }
        userService.checkToken(authorization, idUser);
        if (passwordEncoder.matches(userChangePassword.getPasswordOld(), userOptional.get().getPassword())) {
            if (userChangePassword.getPasswordNew().equals(userChangePassword.getConfirmPasswordNew())) {
                userOptional.get().setPassword(passwordEncoder.encode(userChangePassword.getPasswordNew()));
                userService.save(userOptional.get());
                return new ResponseEntity<>(userOptional.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ResponseNotification(HttpStatus.BAD_REQUEST.toString(),
                        MessageResponse.RegisterMessage.WRONG_CONFIRM_PASSWORD),
                        HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(new ResponseNotification(HttpStatus.BAD_REQUEST.toString(),
                    MessageResponse.RegisterMessage.WRONG_PASSWORD),
                    HttpStatus.BAD_REQUEST);
        }
    }

    // Đăng nhập
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            final double startTime = System.currentTimeMillis();
            List<User> userBannedList = userService.findAllUserBanned();
            if (!CollectionUtils.isEmpty(userBannedList)) {
                if (userBannedList.stream().anyMatch(item -> item.getUsername().equals(user.getUsername()))) {
                    return new ResponseEntity<>(new ResponseNotification(HttpStatus.BAD_REQUEST.toString(),
                            MessageResponse.LoginMessage.USER_HAS_LOCK),
                            HttpStatus.BAD_REQUEST);
                }
            }
            Iterable<User> list = userService.findAll();
            List<User> userList = (List<User>) list;
            List<String> userName = userList.stream().map(User::getUsername).collect(Collectors.toList());

            for (String s : userName) {
                if (user.getUsername().toUpperCase().equals(s) || user.getUsername().toLowerCase().equals(s)) {
                    if (!user.getUsername().equals(s)) {
                        return new ResponseEntity<>(new ResponseNotification(HttpStatus.BAD_REQUEST.toString(),
                                MessageResponse.LoginMessage.USER_NAME),
                                HttpStatus.BAD_REQUEST);
                    }
                }
            }
            if (userName.stream().noneMatch(item -> item.equals(user.getUsername()))) {
                return new ResponseEntity<>(new ResponseNotification(HttpStatus.BAD_REQUEST.toString(),
                        MessageResponse.LoginMessage.USER_NAME),
                        HttpStatus.BAD_REQUEST);
            }
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = jwtService.generateTokenLogin(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User currentUser = userService.findByUsername(user.getUsername());

            Optional<VerificationToken> verificationToken = verificationTokenRepository.findByUserId(currentUser.getId());
            if (!verificationToken.isPresent()) {
                VerificationToken verificationToken1 = new VerificationToken();
                verificationToken1.setToken(jwt);
                verificationToken1.setCreatedDate(new Date());
                verificationToken1.setUser(currentUser);
                verificationTokenRepository.save(verificationToken1);
            } else {
                verificationToken.get().setCreatedDate(new Date());
                verificationToken.get().setToken(jwt);
                verificationTokenRepository.save(verificationToken.get());
            }

            final double elapsedTimeMillis = System.currentTimeMillis();
            log.info(Constants.MESSAGE_STRIKE_THROUGH);
            Common.executionTime(startTime, elapsedTimeMillis);
            log.info(Constants.MESSAGE_STRIKE_THROUGH);
            return ResponseEntity.ok(new JwtResponse(jwt, currentUser.getId(), userDetails.getUsername(), userDetails.getAuthorities()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ResponseNotification(HttpStatus.BAD_REQUEST.toString(),
                MessageResponse.RegisterMessage.WRONG_PASSWORD),
                HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/users/{idUser}")
    public ResponseEntity<?> getProfile(@PathVariable Long idUser) {
        Optional<User> userOptional = userService.findById(idUser);
        if (!userOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_USER, idUser), HttpStatus.NOT_FOUND);
        }
        List<User> userBannedList = userService.findAllUserBanned();
        if (!CollectionUtils.isEmpty(userBannedList)) {
            if (userBannedList.stream().anyMatch(item -> item.getUsername().equals(userOptional.get().getUsername()))) {
                return new ResponseEntity<>(new ResponseNotification(HttpStatus.BAD_REQUEST.toString(),
                        MessageResponse.LoginMessage.USER_HAS_LOCK),
                        HttpStatus.BAD_REQUEST);
            }
        }
        UserDTO userDTO = modelMapper.map(userService.checkUser(idUser), UserDTO.class);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping("/findById/{idUser}")
    public ResponseEntity<?> findById(@PathVariable Long idUser) {
        if (userService.checkUser(idUser) == null) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_USER, idUser), HttpStatus.NOT_FOUND);
        }
        NameDTO nameDTO = modelMapper.map(userService.checkUser(idUser), NameDTO.class);
        return new ResponseEntity<>(nameDTO, HttpStatus.OK);
    }

    // Sửa thông tin
    @Transactional
    @PutMapping("/users/{idUser}")
    public ResponseEntity<?> updateUserProfile(@PathVariable Long idUser,
                                               @RequestBody User user,
                                               @RequestHeader("Authorization") String authorization) {
        if (StringUtils.isEmpty(user.getFullName()) || StringUtils.isEmpty(user.getPhone())) {
            return new ResponseEntity<>(new ResponseNotification(HttpStatus.BAD_REQUEST.toString(),
                    MessageResponse.DESCRIPTION_BLANK),
                    HttpStatus.BAD_REQUEST);
        }
        Optional<User> userOptional = this.userService.findById(idUser);
        if (!userOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_USER, idUser), HttpStatus.NOT_FOUND);
        }
        userService.checkToken(authorization, idUser);
        String avatarName = "";
        List<ListAvatarDefault> listAvatarDefaults = userService.listAvatar();
        for (ListAvatarDefault listAvatarDefault : listAvatarDefaults) {
            if (user.getAvatar().equals(listAvatarDefault.getName())) {
                avatarName = listAvatarDefault.getName();
                break;
            }
        }
        if (avatarName.equals("")) {
            if (user.getAvatar() != null || !user.getAvatar().equals(Constants.BLANK)) {
                userOptional.get().setAvatar(user.getAvatar());
                if (!userOptional.get().getAvatar().equals(user.getAvatar())) {
                    Image image = imageService.createImageDefault(user.getAvatar(), user);
                    imageService.save(image);
                }
                Optional<LastUserLogin> lastUserLogin = lastUserLoginRepository.findAllByIdUser(idUser);
                lastUserLogin.ifPresent(userLogin -> userLogin.setAvatar(user.getAvatar()));
            }
            if (user.getCover() != null || !(user.getCover().equals(Constants.BLANK))
                    || !(userOptional.get().getCover().equals(Constants.ImageDefault.DEFAULT_BACKGROUND_3))) {
                Image image = imageService.createImageDefault(user.getCover(), user);
                imageService.save(image);
            }
        } else {
            userOptional.get().setAvatar(avatarName);
            Optional<LastUserLogin> lastUserLogin = lastUserLoginRepository.findAllByIdUser(idUser);
            if (lastUserLogin.isPresent()) {
                lastUserLogin.get().setAvatar(avatarName);
            }
        }
        if (!userOptional.get().getCover().equals(user.getCover())) {
            userOptional.get().setCover(user.getCover());
            Image image = imageService.createImageDefault(user.getCover(), user);
            imageService.save(image);
        }
        userOptional.get().setAddress(user.getAddress());
        userOptional.get().setPhone(user.getPhone());
        userOptional.get().setFavorite(user.getFavorite());
        userService.save(userOptional.get());
        return new ResponseEntity<>(userOptional.get(), HttpStatus.OK);
    }

    @DeleteMapping("/changeStatusUser")
    public ResponseEntity<?> changeStatusUserActive(@RequestParam Long idUser,
                                                    @RequestParam String type,
                                                    @RequestHeader("Authorization") String authorization) {
        Optional<User> userOptional = userService.findById(idUser);
        if (!userOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_USER, idUser), HttpStatus.NOT_FOUND);
        }
        userService.checkToken(authorization, idUser);
        if ("active".equalsIgnoreCase(type)) {
            if (userOptional.get().getStatus().equals(Constants.STATUS_ACTIVE)) {
                return new ResponseEntity<>(HttpStatus.OK);
            }
            if (userOptional.get().getId().equals(idUser)) {
                userOptional.get().setStatus(Constants.STATUS_ACTIVE);
                userService.save(userOptional.get());
                return new ResponseEntity<>(userOptional.get(), HttpStatus.OK);
            }
        }
        if ("lock".equalsIgnoreCase(type)) {
            if (userOptional.get().getStatus().equals(Constants.STATUS_LOCK)) {
                return new ResponseEntity<>(HttpStatus.OK);
            }
            if (userOptional.get().getId().equals(idUser)) {
                userOptional.get().setStatus(Constants.STATUS_LOCK);
                userService.save(userOptional.get());
                return new ResponseEntity<>(userOptional.get(), HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(new ResponseNotification(HttpStatus.BAD_REQUEST.toString(),
                MessageResponse.NO_VALID, MessageResponse.DESCRIPTION),
                HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/listImageDefault")
    public ResponseEntity<?> listImageDefault() {
        return new ResponseEntity<>(userService.listAvatar(), HttpStatus.OK);
    }

    @DeleteMapping("/changeImage")
    public ResponseEntity<?> changeImage(@RequestParam Long idUser,
                                         @RequestParam Long idImage,
                                         @RequestParam String type,
                                         @RequestHeader("Authorization") String authorization) {
        Optional<User> userOptional = userService.findById(idUser);
        if (!userOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_USER, idUser), HttpStatus.NOT_FOUND);
        }
        userService.checkToken(authorization, idUser);
        Optional<Image> imageOptional = imageService.findById(idImage);
        if (!imageOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.responseMessage(Constants.IdCheck.ID_IMAGE, idImage),
                    HttpStatus.NOT_FOUND);
        }
        if (Constants.AVATAR.equals(type)) {
            userOptional.get().setAvatar(imageOptional.get().getLinkImage());
            Optional<LastUserLogin> lastUserLogin = lastUserLoginRepository.findAllByIdUser(idUser);
            lastUserLogin.ifPresent(userLogin -> userLogin.setAvatar(imageOptional.get().getLinkImage()));
        }
        if (Constants.COVER.equals(type)) {
            userOptional.get().setCover(imageOptional.get().getLinkImage());
        }
        userService.save(userOptional.get());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
