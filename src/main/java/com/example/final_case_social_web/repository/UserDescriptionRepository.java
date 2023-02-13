package com.example.final_case_social_web.repository;

import com.example.final_case_social_web.model.UserDescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDescriptionRepository extends JpaRepository<UserDescription, Long> {

    List<UserDescription> findAllByUserIdOrderByCreateAtDesc(Long user_id);
}
