package com.example.final_case_social_web.repository;

import com.example.final_case_social_web.model.TestData2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestData2Repository extends JpaRepository<TestData2, Long> {
}
