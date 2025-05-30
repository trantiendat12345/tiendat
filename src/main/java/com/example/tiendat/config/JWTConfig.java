package com.example.tiendat.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JWTConfig {
    
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private Long expirationTime;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.expirationRefreshToken}")
    private Long refreshTokenExpirationTime;

    public String getSecretKey() {
        return secretKey;
    }

    public Long getExpirationTime() {
        return expirationTime;
    }

    public String getIssue() {
        return issuer;
    }

    public Long getRefreshTokenExpirationTime() {
        return refreshTokenExpirationTime;
    }

}
