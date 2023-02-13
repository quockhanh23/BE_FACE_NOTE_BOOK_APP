package com.example.final_case_social_web.repository;

import com.example.final_case_social_web.model.TheGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TheGroupRepository extends JpaRepository<TheGroup, Long> {

    List<TheGroup> findByIdUserCreate(Long idUserCreate);

    List<TheGroup> findAllByIdIn(List<Long> id);

    @Modifying
    @Query(value = "select *\n" +
            "from the_group\n" +
            "where id_user_create not in (select id_user_create from the_group where id_user_create = :idUserCreate)\n" +
            "  and status = 'Public'", nativeQuery = true)
    List<TheGroup> findAllGroup(@Param("idUserCreate") Long idUserCreate);

    @Modifying
    @Query(value = "SELECT *\n" +
            "FROM the_group tg\n" +
            "WHERE (tg.group_name LIKE concat('%', :searchText, '%') or tg.type LIKE concat('%', :searchText, '%')) and (tg.id_user_create !=:idUser)", nativeQuery = true)
    List<TheGroup> searchAllByGroupNameAndType(@Param("searchText") String searchText, @Param("idUser") Long idUser);
}
