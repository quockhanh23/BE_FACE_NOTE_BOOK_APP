package com.example.final_case_social_web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NameDTO {
    private Long id;
    private String fullName;
    private Date createAt;
    private String address;
    private String avatar;
}
