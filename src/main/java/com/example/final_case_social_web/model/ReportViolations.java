package com.example.final_case_social_web.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
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

    @ManyToOne
    @JoinColumn(name = "userReport_id")
    private User userReport;

    @ManyToOne
    @JoinColumn(name = "userViolate_id")
    private User userViolate;

    @ManyToOne
    @JoinColumn(name = "postViolate_id")
    private Post2 postViolate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "theGroupViolate_id")
    private TheGroup theGroupViolate;
}
