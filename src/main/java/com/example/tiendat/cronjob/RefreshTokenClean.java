package com.example.tiendat.cronjob;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.tiendat.modules.users.repositories.RefreshTokenRepository;

@Service
public class RefreshTokenClean {
    
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    private static final Logger logger = LoggerFactory.getLogger(RefreshTokenClean.class);

    @Transactional
    @Scheduled(cron = "0 0 0 * * ?")
    public void cleanupExpriedTokens() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        int deletedCount = refreshTokenRepository.deleteByExpiryDateBefore(currentDateTime);

        logger.info("DA XOA: " + deletedCount + " tokens het han");
    }

}
