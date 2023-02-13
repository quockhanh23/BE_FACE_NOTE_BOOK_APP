package com.example.final_case_social_web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversationDTO {
    private Long id;
    private Date createAt;
    private String statusConversation;
    private UserDTO idSender;
    private UserDTO idReceiver;
}
