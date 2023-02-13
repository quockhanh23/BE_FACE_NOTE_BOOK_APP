package com.example.final_case_social_web.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@Table(name = "image")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Lob
    private String linkImage;
    private String status;
    private Date createAt = new Date();
    private Date deleteAt;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
