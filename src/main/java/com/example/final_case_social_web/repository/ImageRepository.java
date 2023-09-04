package com.example.final_case_social_web.repository;

import com.example.final_case_social_web.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    void delete(Image entity);

    @Modifying
    @Query(value = "select * from image where (status = 'Public' or status = 'Private') and (delete_at is null) and user_id = :idUser", nativeQuery = true)
    List<Image> findAllImageByIdUser(@Param("idUser") Long idUser);

    @Modifying
    @Query(value = "select * from image where delete_at is not null and status = 'Delete' and user_id = :idUser", nativeQuery = true)
    List<Image> findAllImageDeletedByUserId(@Param("idUser") Long idUser);
}
