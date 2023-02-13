package com.example.final_case_social_web.service;

import com.example.final_case_social_web.model.GroupParticipant;
import com.example.final_case_social_web.model.TheGroup;
import com.example.final_case_social_web.model.User;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GroupParticipantService extends GeneralService<GroupParticipant> {

    List<GroupParticipant> findAllUserStatusPendingApproval(Long idTheGroup);

    List<GroupParticipant> findAllUserStatusApproved(Long idGroup);

    Optional<GroupParticipant> findByUserIdAndTheGroupId(Long user_id, Long theGroup_id);

    List<GroupParticipant> groupJoined(@Param("idUser") Long idUser);

    GroupParticipant createDefault(TheGroup theGroup, User user, String role);
}
