package com.example.final_case_social_web.service;


import com.example.final_case_social_web.dto.ListAvatarDefault;
import com.example.final_case_social_web.dto.UserDTO;
import com.example.final_case_social_web.model.User;
import com.example.final_case_social_web.notification.ResponseNotification;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserService extends UserDetailsService {

    void save(User user);

    List<User> findAllRoleUser();

    Iterable<User> findAll();

    User findByUsername(String username);

    User getCurrentUser();

    Optional<User> findById(Long id);

    UserDetails loadUserById(Long id);

    boolean checkLogin(User user);

    boolean isCorrectConfirmPassword(User user);

    void createDefault(User user);

    UserDTO mapper(User user);

    Object checkAdmin(Long idAdmin);

    User checkUser(Long idUser);

    List<ListAvatarDefault> listAvatar();

    Set<User> findAllByIdIn(@Param("inputList") Set<Long> inputList);

    List<User> friendSuggestion(@Param("idUser") Long idUser);

    List<User> listPeople(@Param("idUser") Long idUser);

    List<User> findAllUserBanned();

    List<User> allFriendByUserId(@Param("idUser") Long idUser);

    List<UserDTO> filterUser(Long idUser, String type);

    List<User> findAllByIdIn(@Param("inputList") List<Long> inputList);

    List<User> findAllByEmailAndFullName(String searchText, @Param("idSendBlock") Long idSendBlock);

    List<User> searchFriend(@Param("searchText") String searchText, @Param("idUser") Long idUser);

    List<User> searchAll(@Param("searchText") String searchText, @Param("idUser") Long idUser);

    ResponseNotification checkExistUserNameAndEmail(User user);

    Optional<User> findUserByEmailAndUserName(@Param("userName") String userName, @Param("email") String email);

    List<UserDTO> listFriend(Long idUser);

    boolean errorToken(String authorization, Long idUser);

    List<User> findAllByEmailOrUsername(String searchText);
}
