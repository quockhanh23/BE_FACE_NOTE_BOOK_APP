package com.example.final_case_social_web.repository;

import com.example.final_case_social_web.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Modifying
    @Query(value = "select * from notification where send_to_id =:idSendTo order by create_at desc ", nativeQuery = true)
    List<Notification> findAllByIdSendTo(@Param("idSendTo") Long idSendTo);

    @Modifying
    @Query(value = "select * from notification where status like 'Not seen' and send_to_id =:idSendTo", nativeQuery = true)
    List<Notification> findAllByIdSendToNotSeen(@Param("idSendTo") Long idSendTo);
}
