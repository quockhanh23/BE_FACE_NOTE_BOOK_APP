package com.example.final_case_social_web.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private String fullName;
    private String email;
    private String phone;
    private String favorite;
    private String avatar;
    private String cover;
    private String address;
    private String gender;
    private String education;
    private LocalDate dateOfBirth;
    private Date createAt;
    private String status;
    private long mutualFriends;
    private Boolean sendRequestFriend = false;
    private Boolean peopleSendRequestFriend = false;
    private long numberRepost;

    public UserDTO(Long id, String fullName, String avatar, String cover) {
        this.id = id;
        this.fullName = fullName;
        this.avatar = avatar;
        this.cover = cover;
    }
}
