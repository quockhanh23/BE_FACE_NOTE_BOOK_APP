package com.example.final_case_social_web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserChangePassword {
    private Long id;
    private String passwordOld;
    private String passwordNew;
    private String confirmPasswordNew;
}
