package com.example.final_case_social_web.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImageGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Lob
    private String linkImage;
    private String status;
    private Date createAt = new Date();
    private Date deleteAt;
    private Long idTheGroup;
    private Long idUserUpLoad;
}
