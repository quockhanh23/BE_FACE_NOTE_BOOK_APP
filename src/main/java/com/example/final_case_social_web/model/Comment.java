package com.example.final_case_social_web.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 500, nullable = false)
    private String content;
    private LocalDateTime createAt;
    private LocalDateTime editAt;
    private boolean isDelete;
    private LocalDateTime deleteAt;
    @Column(length = 1000)
    private String image;
    private Long numberLike;
    private Long numberDisLike;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post2 post;
}
