package com.example.final_case_social_web.service;

import com.example.final_case_social_web.model.BlackList;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BlackListService {

    void create(BlackList blackList);

    void createDefault(BlackList blackList, Long idUserLogin, Long idBlock, String type);

    void delete(BlackList blackList);

    void saveAll(List<BlackList> blackLists);

    Optional<BlackList> findById(Long id);

    Optional<BlackList> findBlock(@Param("idSendBlock") Long idSendBlock, @Param("idBlock") Long idBlock);

    List<BlackList> listBlockedByIdUser(@Param("idSendBlock") Long idSendBlock);
}
