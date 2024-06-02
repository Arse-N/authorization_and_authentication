package com.stellarlabs.authentication_and_authorization_service.service.impl;

import com.stellarlabs.authentication_and_authorization_service.dto.userInfo.UserDataDTO;
import com.stellarlabs.authentication_and_authorization_service.model.User;
import com.stellarlabs.authentication_and_authorization_service.model.UserInfo;
import com.stellarlabs.authentication_and_authorization_service.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserInfoService {

    private final UserInfoRepository userInfoRepository;

    public UserInfo getUser(String uuid){
        return userInfoRepository.findByUserId(uuid);
    }

    public UserDataDTO getUserData(User user){
        UserInfo userInfo = userInfoRepository.findByUserId(user.getUuid());
        UserDataDTO userDataDTO = new UserDataDTO();
        userDataDTO.setUuid(user.getUuid());
        userDataDTO.setName(user.getFirstName());
        userDataDTO.setQuiz(userInfo.isQuiz());
        userDataDTO.setQuestion(userInfo.getQuestion());
        return userDataDTO;
    }

    public void saveUserInfo(UserInfo userInfo){
        userInfoRepository.save(userInfo);
    }

}
