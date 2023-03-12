package com.example.final_case_social_web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostCheck {
    private Long id;
    private Date createAt;
    private Date editAt;
    private String content;
    private String image;
    private String fullName;
    private Long idUser;
}
