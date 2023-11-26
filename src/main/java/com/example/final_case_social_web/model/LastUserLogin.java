package com.example.final_case_social_web.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class LastUserLogin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long idUser;
    private String userName;
    private String fullName;
    @Column(length = 1000)
    private String avatar;
    private Date loginTime;
    private String ipAddress;
}
