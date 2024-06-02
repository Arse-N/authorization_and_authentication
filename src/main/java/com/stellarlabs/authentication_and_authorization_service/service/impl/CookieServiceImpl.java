package com.stellarlabs.authentication_and_authorization_service.service.impl;

import com.stellarlabs.authentication_and_authorization_service.service.CookieService;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.stream.Collectors;

@Service
public class CookieServiceImpl implements CookieService {

    public void createCookie(HttpServletResponse response, String value, long maxAge) {
        response.setHeader("Set-Cookie", "jwt=" + value + ";Max-Age=" + maxAge + "; Path=/;HttpOnly;Secure;SameSite=Lax");
    }

    @Override
    public String getCookie(String value) {
        if(value == null){
            throw new NullPointerException("Can't found cookie");
        }else{
            return value;
        }
    }

    @Override
    public String getJwtFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            String c = Arrays.stream(cookies)
                    .filter(cookie -> cookie.getName().equals("jwt"))
                    .map(Cookie::getValue)
                    .collect(Collectors.joining(""));
            return getCookie(c);
        } else {
            throw new NullPointerException("Can't find cookie");
        }
    }

}
