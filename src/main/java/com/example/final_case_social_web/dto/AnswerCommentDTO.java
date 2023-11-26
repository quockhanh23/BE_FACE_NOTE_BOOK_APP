package com.example.final_case_social_web.dto;

import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
public class AnswerCommentDTO {
    private String content;
    private LocalDateTime createAt;
    private LocalDateTime editAt;
    private String image;
    private CommentDTO commentDTO;
    private UserDTO userDTO;
}
