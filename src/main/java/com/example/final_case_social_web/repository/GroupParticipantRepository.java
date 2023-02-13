package com.example.final_case_social_web.repository;

import com.example.final_case_social_web.model.GroupParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupParticipantRepository extends JpaRepository<GroupParticipant, Long> {

    Optional<GroupParticipant> findByUserIdAndTheGroupId(Long user_id, Long theGroup_id);

    @Modifying
    @Query(value = "select * from group_participant where status = 'Pending approval' and the_group_id = :idGroup", nativeQuery = true)
    List<GroupParticipant> findAllUserStatusPendingApproval(@Param("idGroup") Long idGroup);

    @Modifying
    @Query(value = "select * from group_participant where status = 'Approved' and the_group_id = :idGroup", nativeQuery = true)
    List<GroupParticipant> findAllUserStatusApproved(@Param("idGroup") Long idGroup);

    @Query(value = "select * from group_participant where user_id =:idUser and status = 'Approved'", nativeQuery = true)
    List<GroupParticipant> groupJoined(@Param("idUser") Long idUser);
}
