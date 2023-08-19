package com.example.final_case_social_web.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FriendRelationDTO {
    private Long id;
    private Long idUser;
    private Long idFriend;
    private String statusFriend;
    private UserDTO friend;
    private UserDTO userLogin;
}
