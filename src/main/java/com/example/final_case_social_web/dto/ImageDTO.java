package com.example.final_case_social_web.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImageDTO {
    private Long id;
    private String linkImage;
    private String status;
    private Date createAt;
    private Date deleteAt;
}
