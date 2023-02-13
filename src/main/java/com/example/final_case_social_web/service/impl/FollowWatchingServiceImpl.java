package com.example.final_case_social_web.service.impl;

import com.example.final_case_social_web.common.Constants;
import com.example.final_case_social_web.model.FollowWatching;
import com.example.final_case_social_web.model.User;
import com.example.final_case_social_web.notification.ResponseNotification;
import com.example.final_case_social_web.repository.FollowWatchingRepository;
import com.example.final_case_social_web.repository.UserRepository;
import com.example.final_case_social_web.service.FollowWatchingService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class FollowWatchingServiceImpl implements FollowWatchingService {

    @Autowired
    private FollowWatchingRepository followWatchingRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void createFollow(Long idUserLogin, Long idUserWatching) {
        if (idUserLogin != null && idUserWatching != null) {
            List<FollowWatching> watchingList = followWatchingRepository.findOne(idUserLogin, idUserWatching);
            if (CollectionUtils.isEmpty(watchingList)) {
                FollowWatching watching = new FollowWatching();
                watching.setCreateAt(new Date());
                watching.setIdUser(idUserLogin);
                watching.setStatus(Constants.FollowPeople.FOLLOW);
                watching.setIdUserTarget(idUserWatching);
                followWatchingRepository.save(watching);
            } else {
                watchingList.get(0).setStatus(Constants.FollowPeople.FOLLOW);
                followWatchingRepository.save(watchingList.get(0));
            }
        }
    }

    @Override
    public void unFollow(Long idUserLogin, Long idUserWatching) {
        if (idUserLogin != null && idUserWatching != null) {
            List<FollowWatching> watchingList = followWatchingRepository.findOne(idUserLogin, idUserWatching);
            if (!CollectionUtils.isEmpty(watchingList)) {
                watchingList.get(0).setStatus(Constants.FollowPeople.UNFOLLOW);
                followWatchingRepository.save(watchingList.get(0));
            }
        }
    }

    @Override
    public Object checkUserHadFollow(Long idUserLogin, Long idUserFollow) {
        Optional<User> userOptionalLogin = userRepository.findById(idUserLogin);
        Optional<User> userOptionalFollow = userRepository.findById(idUserFollow);
        if (!userOptionalLogin.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_USER, userOptionalLogin.get().getId()), HttpStatus.NOT_FOUND);
        }
        if (!userOptionalFollow.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_USER, userOptionalFollow.get().getId()), HttpStatus.NOT_FOUND);
        }
        List<FollowWatching> followWatchingList = followWatchingRepository.findOne(idUserLogin, idUserFollow);
        if (!CollectionUtils.isEmpty(followWatchingList)) {
            if (userOptionalLogin.get().getId().equals(followWatchingList.get(0).getIdUser())
                    && userOptionalFollow.get().getId().equals(followWatchingList.get(0).getIdUserTarget())) {
                return userOptionalFollow;
            }
        }
        return null;
    }
}
