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
public class LifeEvents {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String work;
    private String status;
    private Date createAt;
    private Date editAt;
    private Date timeline;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
