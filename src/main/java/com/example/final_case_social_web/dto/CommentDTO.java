package com.example.final_case_social_web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private String content;
    private LocalDateTime createAt;
    private LocalDateTime editAt;
    private String image;
    private Long numberLike;
    private Long numberDisLike;
    private PostDTO postDTO;
    private UserDTO userDTO;
}
