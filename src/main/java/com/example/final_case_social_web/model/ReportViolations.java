package com.example.final_case_social_web.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class ReportViolations {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private String type;
    private String reason;
    private String status;
    private Date createAt;
    private Date editAt;
    private Long idUserReport;
    private Long idViolate;
}
