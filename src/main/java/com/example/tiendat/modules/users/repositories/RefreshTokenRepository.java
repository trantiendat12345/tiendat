package com.example.tiendat.modules.users.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.tiendat.modules.users.entities.RefreshToken;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    boolean existsByRefreshToken(String refreshToken);

    Optional<RefreshToken> findByRefreshToken(String refreshToken);
    Optional<RefreshToken> findByUserId(Long userId);

    
}
