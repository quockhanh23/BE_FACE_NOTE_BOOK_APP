package com.example.final_case_social_web.repository;

import com.example.final_case_social_web.model.ReportViolations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<ReportViolations, Long> {

    List<ReportViolations> findAllByIdUserReportAndIdViolate(Long idUserReport, Long idViolate);
    List<ReportViolations> findAllByIdViolateAndType(Long idViolate, String type);
}
