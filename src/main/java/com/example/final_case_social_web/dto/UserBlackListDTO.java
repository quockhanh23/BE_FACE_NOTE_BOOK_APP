package com.example.final_case_social_web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBlackListDTO {
    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private String avatar;
    private String cover;
    private String gender;
    private LocalDate dateOfBirt;
}
