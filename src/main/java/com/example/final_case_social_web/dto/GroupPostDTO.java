package com.example.final_case_social_web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupPostDTO {
    private Long id;
    private String status;
    private String createBy;
    private Date createAt;
    private String image;
    private String content;
    private Long idUser;
    private String userName;
    private Long idTheGroup;
    private String groupName;
}
