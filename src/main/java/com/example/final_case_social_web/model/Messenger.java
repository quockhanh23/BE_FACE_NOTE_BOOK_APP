package com.example.final_case_social_web.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Messenger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 300)
    private String content;
    @Column(length = 1000)
    private String image;
    private Date createAt;
    private String format;
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User idSender;
    @ManyToOne
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;
    @ManyToOne
    @JoinColumn(name = "conversationDeleteTime_id")
    private ConversationDeleteTime conversationDeleteTime;
}
