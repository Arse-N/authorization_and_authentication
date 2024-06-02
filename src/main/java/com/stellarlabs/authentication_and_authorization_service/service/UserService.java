package com.stellarlabs.authentication_and_authorization_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.i18n.phonenumbers.NumberParseException;
import com.stellarlabs.authentication_and_authorization_service.dto.account.AccountDTO;
import com.stellarlabs.authentication_and_authorization_service.dto.auth.AuthResponseDTO;
import com.stellarlabs.authentication_and_authorization_service.dto.auth.RegisterDto;
import com.stellarlabs.authentication_and_authorization_service.dto.UserDTO;
import com.stellarlabs.authentication_and_authorization_service.dto.notification.NotificationResponseDto;
import com.stellarlabs.authentication_and_authorization_service.dto.userInfo.UserDataDTO;
import com.stellarlabs.authentication_and_authorization_service.model.User;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

public interface UserService {

    /**
     * receiving user and saving in DB
     *
     * @param user
     * @return {@link  User}
     */
    void saveUser(User user);

//    /**
//     * receiving user id and setting isActive = false and isDeleted = true
//     *
//     * @param id
//     */
//    void deleteUserById(String id) throws JsonProcessingException;

    /**
     * receiving user email and returning all user info with that email.
     *
     * @param email is current user email.
     * @return {@link  User}
     */

    User getUserByEmail(String email);

    /**
     * receiving user uuid and returning all user info with that uuid.
     *
     * @param uuid
     * @return {@link  User}
     */

    User getUserByUuid(String uuid);

    /**
     * receiving user uuid and returning all user info with that passwordToken.
     *
     * @param passwordToken
     * @return {@link  User}
     */

    User getUserByPasswordToken(String passwordToken);

    /**
     * receiving user verification token and returning user info if verification token not null.
     *
     * @param verificationToken is for email verification
     * @return {@link  User}
     */
    User getUserByVerificationCode(String verificationToken);

//    /**
//     * receiving user uuid and returning all AccountDTO info from Account service with that uuid.
//     *
//     * @param uuid
//     * @return {@link  AccountDTO} User info from Account Service
//     */
//    AccountDTO send(String uuid);

    /**
     * receiving user uuid and returning all AccountDTO info from Account service with that uuid.
     *
     * @param user
     * @return {@link  UserDTO} User info from Account Service
     */
    UserDTO convert(User user);

    /**
     * receiving user email and sending welcome message after registration.
     *
     * @param user is current user.
     */

    void getEmail(User user) throws JsonProcessingException;

    boolean existByEmail(String email);


    /**
     * receiving registerDto and converting to User .
     *
     * @param registerDto
     * @return {@link  User} User info from Account Service
     */
    User convert(RegisterDto registerDto);

    /**
     * returning message that about email verification.
     *
     * @param user is for current user.
     * @param token     is for user email verification.
     */
    void emailVerification(User user, String token) throws Exception;

    /**
     * sending message to user email, that contains link with verification token.
     *
     * @param user is current user.
     */
    void verifyEmail(User user) throws Exception;

    /**
     * after mail sending if setting verification token null
     * and isVerified to true.
     *
     * @param user is current user.
     */
    void verify(User user);

    /**
     * returning body for after login jwt token and user info.
     *
     * @param user is current user.
     * @param token is Jwt token.
     */
    AuthResponseDTO authResponse(UserDataDTO user, String token);


//    /**
//     * changing zone by America/LosAngeles,
//     * with format dd-MM-yyyy hh:mm:ss a.
//     */
//    String dateByZone();

    NotificationResponseDto mapToNotifDto(User user);

    void sendToUserNotification(String uuid, String message);

    void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException;

}
