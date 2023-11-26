package com.example.final_case_social_web.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class NameDTO {
    private Long id;
    private String fullName;
    private Date createAt;
    private String address;
    private String avatar;
}
