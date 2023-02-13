package com.example.final_case_social_web.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String status;
    private String createBy;
    private String editBy;
    private Date createAt;
    private Date editAt;
    private Date deleteAt;
    private String image;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(name = "user_create_id")
    private Long idUserCreate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "the_group_id")
    private TheGroup theGroup;
}
