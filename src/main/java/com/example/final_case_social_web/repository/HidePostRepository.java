package com.example.final_case_social_web.repository;

import com.example.final_case_social_web.model.HidePost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HidePostRepository extends JpaRepository<HidePost, Long> {

    List<HidePost> findAllByIdUser(Long idUser);
}
