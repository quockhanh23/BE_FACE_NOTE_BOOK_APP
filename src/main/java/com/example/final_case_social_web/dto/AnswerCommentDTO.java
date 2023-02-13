package com.example.final_case_social_web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerCommentDTO {
    private String content;
    private LocalDateTime createAt;
    private LocalDateTime editAt;
    private String image;
    private CommentDTO commentDTO;
    private UserDTO userDTO;
}
