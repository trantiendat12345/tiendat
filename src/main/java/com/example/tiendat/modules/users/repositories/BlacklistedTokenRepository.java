package com.example.tiendat.modules.users.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.tiendat.modules.users.entities.BlacklistedToken;

import java.time.LocalDateTime;

@Repository
public interface BlacklistedTokenRepository extends JpaRepository<BlacklistedToken, Long> {

    boolean existsByToken(String token);

    int deleteByExpiryDateBefore(LocalDateTime currentDateTime);

}
