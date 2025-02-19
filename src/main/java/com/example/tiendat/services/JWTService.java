package com.example.tiendat.services;

import org.springframework.stereotype.Service;
import java.security.Key; // dung de tao key
import io.jsonwebtoken.security.Keys; // dung de tao key
import java.util.Base64;
import java.util.Date;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Jwts;

import com.example.tiendat.config.JWTConfig;

@Service
public class JWTService {
    
    private final JWTConfig jwtConfig;
    private final Key key;

    public JWTService(JWTConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
        this.key = Keys.hmacShaKeyFor(Base64.getEncoder().encode(jwtConfig.getSecretKey().getBytes())); // tao key
    }

    public String generateToken(Long userId, String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtConfig.getExpirationTime());
        return Jwts.builder()
        .setSubject(String.valueOf(userId))
        .claim("email", email)
        .setIssuedAt(now)
        .setExpiration(expiryDate)
        .signWith(key, SignatureAlgorithm.HS512)
        .compact();
    }

}
