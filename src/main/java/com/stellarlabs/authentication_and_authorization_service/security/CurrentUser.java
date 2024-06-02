package com.stellarlabs.authentication_and_authorization_service.security;

import com.stellarlabs.authentication_and_authorization_service.model.User;
import org.springframework.security.core.authority.AuthorityUtils;

public class CurrentUser extends org.springframework.security.core.userdetails.User {

    private final User user;

    public CurrentUser(User user) {
        super(user.getEmail(), user.getPassword(), AuthorityUtils.createAuthorityList("User"));
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
