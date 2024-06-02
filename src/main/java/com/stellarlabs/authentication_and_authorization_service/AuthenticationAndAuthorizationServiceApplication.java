package com.stellarlabs.authentication_and_authorization_service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@RequiredArgsConstructor
public class AuthenticationAndAuthorizationServiceApplication{

    public static void main(String[] args) {
        SpringApplication.run(AuthenticationAndAuthorizationServiceApplication.class, args);
    }
}
