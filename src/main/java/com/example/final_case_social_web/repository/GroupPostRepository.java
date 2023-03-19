package com.example.final_case_social_web.repository;

import com.example.final_case_social_web.model.GroupPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupPostRepository extends JpaRepository<GroupPost, Long> {
    @Modifying
    @Query(value = "select * from group_post where the_group_id = :idGroup and status like 'Approved'", nativeQuery = true)
    List<GroupPost> findAllPostByIdGroup(@Param("idGroup") Long idGroup);

    @Modifying
    @Query(value = "select * from group_post where the_group_id = :idGroup and status like 'Pending approval'", nativeQuery = true)
    List<GroupPost> findAllPostWaiting(@Param("idGroup") Long idGroup);

    List<GroupPost> findAllByTheGroupId(@Param("idGroup") Long idGroup);
}
