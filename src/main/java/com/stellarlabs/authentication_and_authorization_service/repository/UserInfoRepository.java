package com.stellarlabs.authentication_and_authorization_service.repository;

import com.stellarlabs.authentication_and_authorization_service.model.UserInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories
//@Repository
public interface UserInfoRepository extends MongoRepository<UserInfo, String> {

    UserInfo findByUserId(String uuid);

}
