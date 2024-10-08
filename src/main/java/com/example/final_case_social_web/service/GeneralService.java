package com.example.final_case_social_web.service;

import java.util.Optional;

public interface GeneralService<T> {

    Optional<T> findById(Long id);

    T save(T t);
}
