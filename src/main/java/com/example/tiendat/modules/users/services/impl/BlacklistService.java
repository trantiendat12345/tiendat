package com.example.tiendat.modules.users.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.tiendat.modules.users.repositories.BlacklistedTokenRepository;

import java.time.ZoneId;
import java.util.Date;

import com.example.tiendat.modules.users.entities.BlacklistedToken;
import com.example.tiendat.modules.users.requests.BlacklistTokenRequest;
import com.example.tiendat.services.JWTService;
import com.example.tiendat.resources.ApiResource;
import com.example.tiendat.resources.MessageResource;

import io.jsonwebtoken.Claims;

@Service
public class BlacklistService {

    @Autowired
    private BlacklistedTokenRepository blacklistedTokenRepository;
    
    @Autowired
    private JWTService jwtService;

    private static final Logger logger = LoggerFactory.getLogger(BlacklistService.class);

    public Object create (BlacklistTokenRequest request) {

        try {

            logger.info("Service");
            
            if(blacklistedTokenRepository.existsByToken(request.getToken())) {

                return ApiResource.error("TOKEN_ERROR", "Token da ton tai", HttpStatus.BAD_REQUEST);

            }

            Claims claims = jwtService.getAllClaimsFromToken(request.getToken());

            Long userId = Long.valueOf(claims.getSubject());

            Date expiryDate = claims.getExpiration();

            BlacklistedToken blacklistedToken = new BlacklistedToken();
            blacklistedToken.setToken(request.getToken());
            blacklistedToken.setUserId(userId);
            blacklistedToken.setExpiryDate(expiryDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            blacklistedTokenRepository.save(blacklistedToken);

            logger.info("Them token vao blacklist thanh cong");

            return new MessageResource("Them token vao blacklist thanh cong");

        } catch (Exception e) {

            return new MessageResource("Network Error!" + e.getMessage());

        }

    }

}
