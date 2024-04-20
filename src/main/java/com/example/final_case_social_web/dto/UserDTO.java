package com.example.final_case_social_web.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @JsonIgnore
    // Lỗi xảy ra ở đây, ACCEPT_CASE_INSENSITIVE_VALUES: json không đọc được LocalDate, đã thử nhiều cách nhưng không được
    private LocalDate dateOfBirth;
    private Date dateOfBirth2;
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

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", favorite='" + favorite + '\'' +
                ", avatar='" + avatar + '\'' +
                ", cover='" + cover + '\'' +
                ", address='" + address + '\'' +
                ", gender='" + gender + '\'' +
                ", education='" + education + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", dateOfBirth2=" + dateOfBirth2 +
                ", createAt=" + createAt +
                ", status='" + status + '\'' +
                ", mutualFriends=" + mutualFriends +
                ", sendRequestFriend=" + sendRequestFriend +
                ", peopleSendRequestFriend=" + peopleSendRequestFriend +
                ", numberRepost=" + numberRepost +
                '}';
    }
}
