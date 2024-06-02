package com.stellarlabs.authentication_and_authorization_service.security;

import com.stellarlabs.authentication_and_authorization_service.exception.customExceptions.UserNotFoundException;
import com.stellarlabs.authentication_and_authorization_service.model.User;
import com.stellarlabs.authentication_and_authorization_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CurrentUserDetailServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            return new CurrentUser(user);
        } else {
            throw new UserNotFoundException("User not found");
        }

    }

}
