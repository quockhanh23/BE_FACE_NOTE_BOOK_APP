package com.example.final_case_social_web.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
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
