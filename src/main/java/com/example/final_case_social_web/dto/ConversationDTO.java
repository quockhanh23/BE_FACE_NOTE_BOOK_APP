package com.example.final_case_social_web.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConversationDTO {
    private Long id;
    private Date createAt;
    private String statusConversation;
    private UserDTO idSender;
    private UserDTO idReceiver;
}
