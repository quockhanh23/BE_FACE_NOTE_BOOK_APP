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
public class Saved {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long idUser;
    private String status;
    private Date saveDate;
    private String type;
    private Long idPost;
    private String userCreate;
    private String groupName;
    private String imagePost;
    private String content;
}
