package com.example.final_case_social_web.service;

public interface FollowWatchingService {

    void createFollow(Long idUserLogin, Long idUserFollow);

    void unFollow(Long idUserLogin, Long idUserFollow);

    Object checkUserHadFollow(Long idUserLogin, Long idUserFollow);
}
