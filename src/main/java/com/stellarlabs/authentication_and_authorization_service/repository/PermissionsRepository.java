package com.stellarlabs.authentication_and_authorization_service.repository;

import com.stellarlabs.authentication_and_authorization_service.model.Permissions;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;



public interface PermissionsRepository extends MongoRepository<Permissions, String> {

    Permissions findById(Long id);

}
