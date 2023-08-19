package com.example.final_case_social_web.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
    private Long id;
    private Date createAt;
    private Date editAt;
    private String content;
    private Long numberLike;
    private Long numberDisLike;
    private String image;
    private Long iconHeart;
    private String status;
    private UserDTO userDTO;
    private long countAllComment;
}
