package com.example.final_case_social_web.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class UserChangePassword {
    private Long id;
    private String passwordOld;
    private String passwordNew;
    private String confirmPasswordNew;
}
