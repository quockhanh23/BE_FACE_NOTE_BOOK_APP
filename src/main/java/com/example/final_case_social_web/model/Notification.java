package com.example.final_case_social_web.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String status;
    @ManyToOne
    @JoinColumn(name = "send_to_id")
    private User idSendTo;
    @ManyToOne
    @JoinColumn(name = "action_id")
    private User idAction;
    private Long typeId;
    private String type;
    private Date createAt;
}
