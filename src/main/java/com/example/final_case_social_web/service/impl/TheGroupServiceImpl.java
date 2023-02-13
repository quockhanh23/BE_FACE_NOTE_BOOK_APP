package com.example.final_case_social_web.service.impl;

import com.example.final_case_social_web.model.TheGroup;
import com.example.final_case_social_web.repository.TheGroupRepository;
import com.example.final_case_social_web.service.TheGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TheGroupServiceImpl implements TheGroupService {

    @Autowired
    private TheGroupRepository theGroupRepository;

    @Override
    public Iterable<TheGroup> findAll() {
        return theGroupRepository.findAll();
    }

    @Override
    public Optional<TheGroup> findById(Long id) {
        return theGroupRepository.findById(id);
    }

    @Override
    public TheGroup save(TheGroup theGroup) {
        return theGroupRepository.save(theGroup);
    }

    @Override
    public List<TheGroup> findByIdUserCreate(Long idUserCreate) {
        return theGroupRepository.findByIdUserCreate(idUserCreate);
    }

    @Override
    public List<TheGroup> findAllGroup(Long idUserCreate) {
        return theGroupRepository.findAllGroup(idUserCreate);
    }

    @Override
    public List<TheGroup> findAllByIdIn(List<Long> id) {
        return theGroupRepository.findAllByIdIn(id);
    }

    @Override
    public List<TheGroup> searchAllByGroupNameAndType(String searchText, Long idUser) {
        return theGroupRepository.searchAllByGroupNameAndType(searchText, idUser);
    }
}
