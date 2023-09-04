package com.example.final_case_social_web.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class DisLikeComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date createAt;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userDisLike;
    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;
}
