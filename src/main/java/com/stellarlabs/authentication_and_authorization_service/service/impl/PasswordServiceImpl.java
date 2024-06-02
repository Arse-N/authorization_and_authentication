package com.stellarlabs.authentication_and_authorization_service.service.impl;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.stellarlabs.authentication_and_authorization_service.dto.error.Message;
import com.stellarlabs.authentication_and_authorization_service.dto.notification.NotificationResponseDto;
import com.stellarlabs.authentication_and_authorization_service.dto.password.PasswordResetDTO;
import com.stellarlabs.authentication_and_authorization_service.email.SendGridService;
import com.stellarlabs.authentication_and_authorization_service.exception.customExceptions.*;
import com.stellarlabs.authentication_and_authorization_service.model.User;
import com.stellarlabs.authentication_and_authorization_service.repository.UserRepository;
import com.stellarlabs.authentication_and_authorization_service.security.JwtTokenUtil;
//import com.stellarlabs.authentication_and_authorization_service.service.KafkaService;
import com.stellarlabs.authentication_and_authorization_service.service.PasswordService;
import com.stellarlabs.authentication_and_authorization_service.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;


@Slf4j
@Component
@RequiredArgsConstructor
public class PasswordServiceImpl implements PasswordService {

    @Value("${jwt.header}")
    private String header;

    @Value("${front-url}")
    private String frontURL;

    private final JwtTokenUtil jwtTokenUtil;

    private final SendGridService sendGridService;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserService userService;

//    private final KafkaService kafkaService;

    @Override
    public void updatePasswordToken(String email, String token) throws UsernameNotFoundException {
        if (userRepository.findByEmail(email) != null) {
            User user = userRepository.findByEmail(email);
            user.setPasswordToken(token);
            user.setExpirationTime(LocalDateTime.now());
            userRepository.save(user);
        } else {
            throw new UsernameNotFoundException("This email is not find:(");
        }
    }

    @Override
    public void updateResetPassword(User user, String password) {
        LocalDateTime now = LocalDateTime.now();

        user.setPassword(passwordEncoder.encode(password));
        user.setPasswordToken(null);
        userRepository.save(user);
    }


    @Override
    public void updateOldPassword(String email, String oldPassword, String newPassword) {
        if (userRepository.findByEmail(email)!= null) {
            User user = userRepository.findByEmail(email);
            if (passwordEncoder.matches(oldPassword, user.getPassword())) {
                user.setPassword(passwordEncoder.encode(newPassword));
                userService.saveUser(user);
            } else
                throw new WrongPasswordException("Password is wrong");
        } else {
            throw new NullPointerException("User not found");
        }
    }

    public void getEmail(User user, String token) throws Exception {
        String link =frontURL + "/auth/reset-password?token=" + token;
        String subject = "Here's the link to reset your password";
        NotificationResponseDto notificationResponseDto = userService.mapToNotifDto(user);
        notificationResponseDto.setLink(link);
        notificationResponseDto.setSubject(subject);
        notificationResponseDto.setCode(1001);
        sendGridService.sendEmail(user.getEmail(), subject, notificationResponseDto);

    }

    public void updateResetPassword(HttpServletRequest request, String oldPassword, String password) {
        String authToken = getJwtFromHeader(request);
        if (!oldPassword.trim().equals("")) {
            String email = jwtTokenUtil.getUsernameFromToken(authToken);
            updateOldPassword(email, oldPassword, password);
            userService.sendToUserNotification(userService.getUserByEmail(email).getUuid(), "Password is successfully reseated.");
            log.info("password is reseated");
        } else {
            throw new PasswordValidationException("Old password must not be blank.");
        }
    }

    public String getJwtFromHeader(HttpServletRequest request) {
        if (request.getHeader(header)!=null) {
            if (request.getHeader(header).startsWith("Bearer ")) {
                return request.getHeader(header).substring(7);
            } else {
                return request.getHeader(header);
            }
        } else {
            throw new UnauthorizedException("User is unauthorized");
        }
    }




    public boolean checkExpirationTime(User user, int minute) {
        long secondsBetween = ChronoUnit.SECONDS.between(user.getExpirationTime(), LocalDateTime.now());
        return secondsBetween <= minute * 60L;
    }


    public void updateForgotPassword(String token, String password) {
        log.info("password is reset from forgot password");
        User user = userService.getUserByPasswordToken(token);

        if (user != null) {
            if (checkExpirationTime(user, 5)) {
                updateResetPassword(user, password);
                userService.sendToUserNotification(user.getUuid(), "Password is successfully reseated.");
            } else {
                user.setPasswordToken(null);
                userService.saveUser(user);
                throw new ExpiredException("Token is already expired. Please try again:)");
            }
        } else {
            throw new NotValidArgumentException("User not found! Token is wrong");
        }

    }

}
