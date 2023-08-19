package com.example.final_case_social_web.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class MessengerDTO {
    private Long id;
    private String content;
    private String statusMessenger;
    private Date createAt;
    private Long idConversation;
}
