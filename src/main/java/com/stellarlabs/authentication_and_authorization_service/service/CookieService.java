package com.stellarlabs.authentication_and_authorization_service.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public interface CookieService {

    /**
     * is creating cookie with value jwt token.
     *
     * @param value is cookie value.
     * @Param maxAge is cookies expiration time.
     */
    void createCookie(HttpServletResponse response, String value, long maxAge);

    /**
     * checking is value null or not.
     *
     * @param value is cookie value.
     */
    String getCookie(String value);

    /**
     * is getting cookies value with name jwt.
     *
     * @param request from front.
     */
    String getJwtFromCookie(HttpServletRequest request);

}
