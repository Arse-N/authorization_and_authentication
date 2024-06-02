package com.stellarlabs.authentication_and_authorization_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.stellarlabs.authentication_and_authorization_service.model.User;

import javax.servlet.http.HttpServletRequest;


public interface PasswordService {

    /**
     * receiving user password token(which is sending from email,when clicking generated link)
     * and updating old one.
     *
     * @param email is current user email.
     * @param token password temporary token for password updating
     */

    void updatePasswordToken(String email, String token);

    /**
     * receiving user new password (when user forgetting current password)
     * and updating old one.
     *
     * @param user is current user who is reseating password
     * @param password receiving new password
     */
    void updateResetPassword(User user, String password);

    /**
     * receiving user email, old password and new password (when user want to change current password)
     * and updating old one.
     *
     * @param email is current user email.
     * @param oldPassword current user password
     * @param newPassword new password
     */
    void updateOldPassword(String email, String oldPassword, String newPassword);

    /**
     * receiving user email, and generated link (which is generating when user forgetting current password)
     * and sending to this email message link .
     *
     * @param user is current user.
     * @param link  generated link
     */
    void getEmail(User user, String link) throws Exception;

    /**
     * is reseating old password from settings
     *
     * @param request     from header
     * @param oldPassword is user current password.
     * @param password    is new password.
     */
    void updateResetPassword(HttpServletRequest request, String oldPassword, String password);

    /**
     * is changing old password from forgot password.
     *
     * @param token    is password token
     * @param password is user's new password.
     */
    void updateForgotPassword(String token, String password);

    /**
     * is checking time after sending mail for forgot password.
     *
     * @param user is current user who is changing password
     * @param minute is time when user can change password.
     */
    boolean checkExpirationTime(User user, int minute);

    String getJwtFromHeader(HttpServletRequest request);



}
