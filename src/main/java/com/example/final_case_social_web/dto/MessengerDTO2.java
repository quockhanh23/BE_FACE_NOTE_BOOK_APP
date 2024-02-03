package com.example.final_case_social_web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor // Phải có
// Nếu để @NoArgsConstructor thì không chạy được
public class MessengerDTO2 {
    private Long id;
    private String content;
}
