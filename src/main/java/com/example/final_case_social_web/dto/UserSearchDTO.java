package com.example.final_case_social_web.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class UserSearchDTO {
    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private String favorite;
    private String avatar;
    private String cover;
}
