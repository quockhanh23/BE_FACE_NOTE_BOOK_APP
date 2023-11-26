package com.example.final_case_social_web.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class SavedDTO {
    private Long id;
    private Long idUser;
    private String status;
    private Date saveDate;
    private PostDTO postDTO;
}
