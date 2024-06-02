package com.stellarlabs.authentication_and_authorization_service.repository;

import com.stellarlabs.authentication_and_authorization_service.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@EnableMongoRepositories
//@Repository
public interface UserRepository extends MongoRepository<User, String> {

//    @Override
//    Optional<User> findById(Long id);
//@Query("{name:'?0'}")
//GroceryItem findItemByName(String name);
//
//    @Query(value="{category:'?0'}", fields="{'name' : 1, 'quantity' : 1}")
//    List<GroceryItem> findAll(String category);
    User findByUuid(String uuid);

    User findByEmail(String email);

    User findByPasswordToken(String passwordToken);

    User findByVerificationToken(String verificationCode);

    boolean existsByEmail(String email);
}
