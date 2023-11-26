package com.example.final_case_social_web.repository;

import com.example.final_case_social_web.model.UserComplaints;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserComplaintsRepository extends JpaRepository<UserComplaints, Long> {
}
