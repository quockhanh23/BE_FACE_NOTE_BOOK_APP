package com.example.final_case_social_web.repository;

import com.example.final_case_social_web.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    Optional<VerificationToken> findByUserId(Long idUser);
}
