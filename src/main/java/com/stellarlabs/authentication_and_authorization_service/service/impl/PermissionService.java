package com.stellarlabs.authentication_and_authorization_service.service.impl;

import com.stellarlabs.authentication_and_authorization_service.model.Permissions;
import com.stellarlabs.authentication_and_authorization_service.repository.PermissionsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final PermissionsRepository permissionsRepository;

    public Permissions getpermission(Long id){
        return permissionsRepository.findById(id);
    }
}
