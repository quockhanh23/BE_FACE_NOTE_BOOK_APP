package com.example.final_case_social_web.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date createAt;
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User idSender;
    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User idReceiver;
}
