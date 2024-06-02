package com.stellarlabs.authentication_and_authorization_service.config;

import com.stellarlabs.authentication_and_authorization_service.security.JwtAuthEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;



@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    private final UserDetailsService userDetailsService;

//    @Autowired
    private final PasswordEncoder passwordEncoder;

//    @Autowired
    private JwtAuthEntryPoint unauthorizedHandler;
//
//    @Value("${user.oauth.clientId}")
//    private String clientID;
//
//    @Value("${user.oauth.clientSecret}")
//    private String clientSecret;
//
//    @Value("${user.oauth.redirectUris}")
//    private String redirectURLs;
//
//    @Value("${user.oauth.accessTokenValidity}")
//    private int accessTokenValidity;
//
//    @Value("${user.oauth.refreshTokenValidity}")
//    private int refreshTokenValidity;


    @Override
    public void configure(HttpSecurity http) throws Exception {
//        Enable cors and disable csrf
        http.csrf().disable().
//                set session management to stateless
                        sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
//                unauthorized requests exception handler
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
                .and()
//               set permissions on endpoints
                .authorizeRequests()
                .antMatchers("/register").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/**").permitAll();


    }

//    Used by spring security if CORS is enabled.


    @Autowired
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }

}
