package com.example.final_case_social_web.controller;

import com.example.final_case_social_web.common.Common;
import com.example.final_case_social_web.common.Constants;
import com.example.final_case_social_web.common.MessageResponse;
import com.example.final_case_social_web.common.Regex;
import com.example.final_case_social_web.dto.*;
import com.example.final_case_social_web.exeption.InvalidException;
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
import org.springframework.validation.FieldError;
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
    public ResponseEntity<?> saveHistoryLogin(@RequestParam Long idUserLogin, @RequestHeader("IP") String IP) {
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Lịch sử đăng nhập local
    @GetMapping("/historyLogin")
    public ResponseEntity<?> getListHistoryLogin() {
        List<LastUserLogin> userLogins = lastUserLoginRepository.historyLogin();
        if (CollectionUtils.isEmpty(userLogins)) userLogins = new ArrayList<>();
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
        search = Common.addEscapeOnSpecialCharactersWhenSearch(search);
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
        search = Common.addEscapeOnSpecialCharactersWhenSearch(search);
        List<UserDTO> userDTOList = userService.listFriend(idUser);
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
            Map<String, String> fieldError = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                fieldError.put(error.getField(), error.getDefaultMessage());
            }
            throw new InvalidException("Không hợp lệ", fieldError);
        }
        ResponseEntity<?> responseEntity = Common.handlerWordsLanguage(user);
        if (null != responseEntity) return responseEntity;
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
        if (responseNotification != null) return new ResponseEntity<>(responseNotification, HttpStatus.BAD_REQUEST);

        Set<Role> roles = new HashSet<>();
        Role role;
        role = (user.getRoles() != null) ?
                roleService.findByName(Constants.Roles.ROLE_ADMIN) : roleService.findByName(Constants.Roles.ROLE_USER);
        roles.add(role);
        user.setRoles(roles);
        userService.createDefault(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setConfirmPassword(passwordEncoder.encode(user.getConfirmPassword()));
        user.setStatus(Constants.STATUS_ACTIVE);
        userService.save(user);
        List<String> listAvatar = Arrays.asList(Constants.ImageDefault.DEFAULT_IMAGE_AVATAR_MALE,
                Constants.ImageDefault.DEFAULT_IMAGE_AVATAR_LGBT, Constants.ImageDefault.DEFAULT_IMAGE_AVATAR_FEMALE);
        List<String> listCover = Arrays.asList(Constants.ImageDefault.DEFAULT_BACKGROUND,
                Constants.ImageDefault.DEFAULT_BACKGROUND_2, Constants.ImageDefault.DEFAULT_BACKGROUND_3);
        if (!listAvatar.contains(user.getAvatar())) {
            Image image = imageService.createImageDefault(user.getAvatar(), user);
            imageService.save(image);
        }
        if (!listCover.contains(user.getCover())) {
            Image image = imageService.createImageDefault(user.getCover(), user);
            imageService.save(image);
        }
//        emailService.sendMail(user.getEmail(), MessageResponse.Email.WELL_COME + user.getFullName(),
//                MessageResponse.Email.THANK);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    // Lấy lại mật khẩu
    @Transactional
    @PostMapping("/passwordRetrieval")
    public ResponseEntity<?> passwordRetrieval(@RequestBody PasswordRetrieval passwordRetrieval,
                                               @RequestHeader("Authorization") String authorization) {
        if (StringUtils.isEmpty(passwordRetrieval.getUserName()) || StringUtils.isEmpty(passwordRetrieval.getEmail())) {
            return new ResponseEntity<>(new ResponseNotification(HttpStatus.BAD_REQUEST.toString(),
                    MessageResponse.IN_VALID), HttpStatus.BAD_REQUEST);
        }
        Optional<User> userOptional = userService
                .findUserByEmailAndUserName(passwordRetrieval.getUserName(), passwordRetrieval.getEmail());
        if (!userOptional.isPresent()) {
            return new ResponseEntity<>(new ResponseNotification(HttpStatus.NOT_FOUND.toString(),
                    MessageResponse.RegisterMessage.PASSWORD_RETRIEVAL_FAIL),
                    HttpStatus.NOT_FOUND);
        }
        boolean check = userService.errorToken(authorization, userOptional.get().getId());
        if (!check) {
            return new ResponseEntity<>(new ResponseNotification(HttpStatus.UNAUTHORIZED.toString(),
                    Constants.TOKEN, Constants.TOKEN + " " + MessageResponse.IN_VALID.toLowerCase()),
                    HttpStatus.UNAUTHORIZED);
        }
        try {
            String newPassword = RandomStringUtils.randomAscii(6);
            userOptional.get().setPassword(passwordEncoder.encode(newPassword));
            userService.save(userOptional.get());
            emailService.sendMail(userOptional.get().getEmail(),
                    MessageResponse.Email.RESET_PASSWORD + MessageResponse.Email.SPACE + MessageResponse.Email.APP,
                    MessageResponse.Email.NEW_PASSWORD + newPassword);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseNotification(HttpStatus.OK.toString(),
                    e.getMessage()), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponseNotification(HttpStatus.OK.toString(),
                MessageResponse.RegisterMessage.PASSWORD_RETRIEVAL + passwordRetrieval.getEmail()),
                HttpStatus.OK);
    }

    // Đổi mật khẩu
    @Transactional
    @PostMapping("/matchPassword")
    public ResponseEntity<?> matchPassword(@RequestBody UserChangePassword userChangePassword,
                                           @RequestParam Long idUser,
                                           @RequestHeader("Authorization") String authorization) {
        try {
            if (!userService.errorToken(authorization, idUser)) {
                return new ResponseEntity<>(new ResponseNotification(HttpStatus.UNAUTHORIZED.toString(),
                        Constants.TOKEN, Constants.TOKEN + " " + MessageResponse.IN_VALID.toLowerCase()),
                        HttpStatus.UNAUTHORIZED);
            }
            Optional<User> userOptional = userService.findById(idUser);
            if (!userOptional.isPresent()) {
                return new ResponseEntity<>(ResponseNotification.
                        responseMessage(Constants.IdCheck.ID_USER, idUser), HttpStatus.NOT_FOUND);
            }
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
            }
            return new ResponseEntity<>(new ResponseNotification(HttpStatus.BAD_REQUEST.toString(),
                    MessageResponse.RegisterMessage.WRONG_PASSWORD),
                    HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseNotification(HttpStatus.BAD_REQUEST.toString(),
                    e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    // Đăng nhập
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            final double startTime = System.currentTimeMillis();
            List<User> userBannedList = userService.findAllUserBanned();
            if (!CollectionUtils.isEmpty(userBannedList)) {
                if (userBannedList.stream().parallel().anyMatch(item -> item.getUsername().equals(user.getUsername()))) {
                    return new ResponseEntity<>(new ResponseNotification(HttpStatus.BAD_REQUEST.toString(),
                            MessageResponse.LoginMessage.USER_HAS_LOCK),
                            HttpStatus.BAD_REQUEST);
                }
            }
            Optional<User> currentUser = userService.findByUsername(user.getUsername());
            if (!currentUser.isPresent()) {
                return new ResponseEntity<>(new ResponseNotification(HttpStatus.BAD_REQUEST.toString(),
                        MessageResponse.LoginMessage.USER_NAME),
                        HttpStatus.BAD_REQUEST);
            }

            Authentication authentication;
            try {
                authentication = authenticationManager
                        .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
            } catch (Exception e) {
                return new ResponseEntity<>(new ResponseNotification(HttpStatus.BAD_REQUEST.toString(),
                        MessageResponse.RegisterMessage.WRONG_PASSWORD),
                        HttpStatus.BAD_REQUEST);
            }

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = jwtService.generateTokenLogin(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            Optional<VerificationToken> verificationToken = verificationTokenRepository
                    .findByUserId(currentUser.get().getId());
            if (!verificationToken.isPresent()) {
                VerificationToken verificationToken1 = new VerificationToken();
                verificationToken1.setToken(jwt);
                verificationToken1.setCreatedDate(new Date());
                verificationToken1.setUser(currentUser.get());
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
            return ResponseEntity.ok(new JwtResponse(jwt, currentUser.get().getId(),
                    userDetails.getUsername(), userDetails.getAuthorities()));
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseNotification(HttpStatus.BAD_REQUEST.toString(),
                    e.getMessage()), HttpStatus.BAD_REQUEST);
        }
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

    @GetMapping("/findNameUserById/{idUser}")
    public ResponseEntity<?> findNameUserById(@PathVariable Long idUser) {
        if (userService.checkUser(idUser) == null) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_USER, idUser), HttpStatus.NOT_FOUND);
        }
        NameDTO nameDTO = modelMapper.map(userService.checkUser(idUser), NameDTO.class);
        return new ResponseEntity<>(nameDTO, HttpStatus.OK);
    }

    // Sửa thông tin người dùng
    @Transactional
    @PutMapping("/users/{idUser}")
    public ResponseEntity<?> updateUserProfile(@PathVariable Long idUser,
                                               @RequestBody User user,
                                               @RequestHeader("Authorization") String authorization) {
        if (StringUtils.isEmpty(user.getFullName()) || StringUtils.isEmpty(user.getPhone())) {
            return new ResponseEntity<>(new ResponseNotification(HttpStatus.BAD_REQUEST.toString(),
                    MessageResponse.DESCRIPTION_BLANK), HttpStatus.BAD_REQUEST);
        }
        if (!Common.checkRegex(user.getPhone(), Regex.CHECK_NUMBER_PHONE)) {
            return new ResponseEntity<>(new ResponseNotification(HttpStatus.BAD_REQUEST.toString(),
                    MessageResponse.WRONG_NUMBER_PHONE), HttpStatus.BAD_REQUEST);
        }
        ResponseEntity<?> responseEntity = Common.handlerWordsLanguage(user);
        if (null != responseEntity) return responseEntity;
        Optional<User> userOptional = this.userService.findById(idUser);
        if (!userOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_USER, idUser), HttpStatus.NOT_FOUND);
        }
        boolean check = userService.errorToken(authorization, idUser);
        if (!check) {
            return new ResponseEntity<>(new ResponseNotification(HttpStatus.UNAUTHORIZED.toString(),
                    Constants.TOKEN, Constants.TOKEN + " " + MessageResponse.IN_VALID.toLowerCase()),
                    HttpStatus.UNAUTHORIZED);
        }
        String avatarName = "";
        boolean checkCover = false;
        List<ListAvatarDefault> listAvatarDefaults = userService.listAvatar();
        for (ListAvatarDefault listAvatarDefault : listAvatarDefaults) {
            if (user.getAvatar().equalsIgnoreCase(listAvatarDefault.getName())) {
                avatarName = listAvatarDefault.getName();
            }
            if (user.getCover().equalsIgnoreCase(listAvatarDefault.getName())) {
                checkCover = true;
            }
        }
        if (avatarName.equals("")) {
            if (StringUtils.isNotEmpty(user.getAvatar())
                    && !user.getAvatar().equalsIgnoreCase(userOptional.get().getAvatar())) {
                userOptional.get().setAvatar(user.getAvatar());
                Image image = imageService.createImageDefault(user.getAvatar(), user);
                imageService.save(image);
                Optional<LastUserLogin> lastUserLogin = lastUserLoginRepository.findAllByIdUser(idUser);
                lastUserLogin.ifPresent(userLogin -> userLogin.setAvatar(user.getAvatar()));
            }
        } else {
            userOptional.get().setAvatar(avatarName);
            Optional<LastUserLogin> lastUserLogin = lastUserLoginRepository.findAllByIdUser(idUser);
            if (lastUserLogin.isPresent()) {
                lastUserLogin.get().setAvatar(avatarName);
            }
        }
        if (!StringUtils.isEmpty(user.getCover())
                && !userOptional.get().getCover().equals(user.getCover())
                && !checkCover) {
            userOptional.get().setCover(user.getCover());
            Image image = imageService.createImageDefault(user.getCover(), user);
            imageService.save(image);
        }
        if (!StringUtils.isEmpty(user.getGender())) {
            userOptional.get().setGender(user.getGender());
        }
        if (user.getDateOfBirth() != null) {
            userOptional.get().setDateOfBirth(user.getDateOfBirth());
        }
        userOptional.get().setAddress(user.getAddress());
        userOptional.get().setPhone(user.getPhone());
        userOptional.get().setFavorite(user.getFavorite());
        userOptional.get().setEducation(user.getEducation());
        userService.save(userOptional.get());
        return new ResponseEntity<>(userOptional.get(), HttpStatus.OK);
    }

    @DeleteMapping("/changeStatusUser")
    public ResponseEntity<?> changeStatusUser(@RequestParam Long idUser,
                                              @RequestParam String type,
                                              @RequestHeader("Authorization") String authorization) {
        boolean check = userService.errorToken(authorization, idUser);
        if (!check) {
            return new ResponseEntity<>(new ResponseNotification(HttpStatus.UNAUTHORIZED.toString(),
                    Constants.TOKEN, Constants.TOKEN + " " + MessageResponse.IN_VALID.toLowerCase()),
                    HttpStatus.UNAUTHORIZED);
        }
        Optional<User> userOptional = userService.findById(idUser);
        if (!userOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_USER, idUser), HttpStatus.NOT_FOUND);
        }
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
                MessageResponse.IN_VALID, MessageResponse.DESCRIPTION),
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
        boolean check = userService.errorToken(authorization, idUser);
        if (!check) {
            return new ResponseEntity<>(new ResponseNotification(HttpStatus.UNAUTHORIZED.toString(),
                    Constants.TOKEN, Constants.TOKEN + " " + MessageResponse.IN_VALID.toLowerCase()),
                    HttpStatus.UNAUTHORIZED);
        }
        Optional<User> userOptional = userService.findById(idUser);
        if (!userOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_USER, idUser), HttpStatus.NOT_FOUND);
        }
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

    @GetMapping("/removeToken")
    public ResponseEntity<?> removeToken(@RequestParam Long idUserLogin) {
        if (idUserLogin == null) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByUserId(idUserLogin);
        if (!verificationToken.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        verificationToken.get().setToken("no token");
        verificationTokenRepository.save(verificationToken.get());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "/list/async")
    public List<User> getAllUsersAsync() {
        userService.doTask();
        return new ArrayList<>();
    }
}
