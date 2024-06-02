package com.stellarlabs.authentication_and_authorization_service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@Slf4j
public class JwtTokenUtil implements Serializable {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * returning username from JWT token
     *
     * @Param token is Jwt token.
     */
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * returning JWT token creation date
     *
     * @Param token is Jwt token.
     */
    public Date getIssuedAtDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getIssuedAt);
    }

    /**
     * returning  JWT token expiration date
     *
     * @Param token is Jwt token.
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    /**
     * returning  JWT token claim, applying the function to get the result
     *
     * @Param token is Jwt token.
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * returning all JWT token claims
     *
     * @Param token is Jwt token.
     */
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * checking token is expired or not
     *
     * @Param token is Jwt token.
     */
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }


    /**
     * generating JWT token
     *
     * @Param email is users personal mail.
     */
    public String generateToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, email);
    }


    /**
     * using HS512 algorithm to generate JWT token
     */
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        final Date createdDate = new Date();
        final Date expirationDate = calculateExpirationDate(createdDate);

        System.out.println("doGenerateToken " + createdDate);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();

    }

    /**
     * checking is jwt token not expired for refreshing
     *
     * @Param token is Jwt token.
     * @Return Boolean value about token expiration.
     */
    public Boolean canTokenBeRefreshed(String token) {
        return (!isTokenExpired(token));
    }

    /**
     * refreshing old jwt token and creating new one
     *
     * @Param token is Jwt token.
     * @Return String Jwt token
     */
    public String refreshToken(String token) {
        log.info("refreshing token ...");
        final Date createdDate = new Date();
        final Date expirationDate = calculateExpirationDate(createdDate);

        final Claims claims = getAllClaimsFromToken(token);
        claims.setIssuedAt(createdDate);
        claims.setExpiration(expirationDate);

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * returning boolean value about JWT token validation
     *
     * @Param token is Jwt token.
     * @Param email is user email.
     * @Return Boolean value
     */
    public Boolean validateToken(String token, String email) {

        final String username = getUsernameFromToken(token);
        return (
                username.equals(email)
                        && !isTokenExpired(token));
    }

    /**
     * calculating and returning expiration date
     *
     * @Param createdDate.
     * @Return Date type value.
     */
    private Date calculateExpirationDate(Date createdDate) {
        log.info("startDate: " + createdDate);
        log.info("expirationDate: " + new Date(createdDate.getTime() + expiration));
        return new Date(createdDate.getTime() + expiration);
    }
}
