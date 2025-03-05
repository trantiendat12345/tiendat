package com.example.tiendat.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Key; // dung de tao key
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

import io.jsonwebtoken.security.Keys; // dung de tao key
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureException;
import io.jsonwebtoken.ExpiredJwtException;

import com.example.tiendat.config.JWTConfig;
import com.example.tiendat.modules.users.repositories.BlacklistedTokenRepository;

@Service
public class JWTService {
    
    private final JWTConfig jwtConfig;
    private final Key key;
    private static final Logger logger = LoggerFactory.getLogger(JWTService.class);

    @Autowired
    private BlacklistedTokenRepository blacklistedTokenRepository;

    public JWTService(JWTConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
        this.key = Keys.hmacShaKeyFor(Base64.getEncoder().encode(jwtConfig.getSecretKey().getBytes())); // tao key
    }

    public String generateToken(Long userId, String email) {

        logger.info("generating...");

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtConfig.getExpirationTime());

        return Jwts.builder()
        .setSubject(String.valueOf(userId))
        .claim("email", email)
        .setIssuer(jwtConfig.getIssue())
        .setIssuedAt(now)
        .setExpiration(expiryDate)
        .signWith(key, SignatureAlgorithm.HS512)
        .compact();
    }

    // public String extractUserName(String token) {
    //     return extractClaim(token, Claims::getSubject);
    // }

    // private <T> T extractClaim(String token, java.util.function.Function<Claims, T> claimsResolver) {
    //     final Claims claims = extractAllClaims(token);
    //     return claimsResolver.apply(claims); // tra ve gia tri cua claim
    // }

    // private Claims extractAllClaims(String token) {
    //     return Jwts.parserBuilder()
    //     .setSigningKey(key)
    //     .build()
    //     .parseClaimsJws(token)
    //     .getBody();
    // }

    public String getUserIdFromJwt(String token) {

        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        
        return claims.getSubject();
    }

    public String getEmailFromJwt(String token) {
        
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        
        return claims.get("email", String.class);
    }

    /*
     * 1. token dung dinh dang khong
     * 2. chu ki cua token co hop le khong
     * 3. token co het han khong
     * 4. user_id cua token co khop voi cai userDetails khong
     * 5. token co chua trong black list khong
     * 6. kiem tra quyen 
     */
    // public boolean isValidToken(String token, UserDetails userDetails) {

    //     try{

    //         // kiem tra dinh dang
    //         if(!isTokenFormatValid(token)) {
    //             logger.error("Token format invalid");
    //             return false;
    //         }

    //         // kiem tra chu ki
    //         if(!isSignatureValid(token)) {
    //             logger.error("Token signature invalid");
    //             return false;
    //         }

    //         // kiem tra het han
    //         if(isTokenExpired(token)) {
    //             logger.error("Token expired");
    //             return false;
    //         }

    //         // kiem tra nguon goc cua token
    //         if(!isIssuerToken(token)) {
    //             logger.error("Token issuer invalid");
    //             return false;
    //         }

    //         //kiem tra xem iserId trong token cos khop voi user_id cua userDetails khong
    //         final String emailFromToken = getEmailFromJwt(token);
    //         if(!emailFromToken.equals(userDetails.getUsername())) {
    //             logger.error("Token user_id invalid");
    //             return false;
    //         }

    //         // kiem tra xem token co trong black list khong


    //     } 
    //     catch (Exception e) {
    //         logger.error("Token invalid" + e.getMessage());
    //         return false;
    //     }
    //     return false;
    // }

    public boolean isTokenFormatValid(String token) {

        try{

            String[] tokenParts = token.split("\\.");
            return tokenParts.length == 3;

        } catch (Exception e) {

            return false;
        }

    }

    public boolean isSignatureValid(String token) {

        try {

            logger.info(token);

            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);

            return true;
        } catch (SignatureException e) {
            return false;
        }
    }

    public Key getSigningKey() {

        byte[] keyBytes = jwtConfig.getSecretKey().getBytes();

        return Keys.hmacShaKeyFor(Base64.getEncoder().encode(keyBytes));

    }

    public boolean isTokenExpired(String token) {

        try {

            // Date now = new Date();

            final Date expiration = getClaimFromToken(token, Claims::getExpiration);

            // logger.info(expiration.toString());

            return expiration.after(new Date());
            
        } catch (ExpiredJwtException e) {
            return false;
        }

    }

    public boolean isIssuerToken(String token) {
        String tokenIssuer = getClaimFromToken(token, Claims::getIssuer);
        return jwtConfig.getIssue().equals(tokenIssuer);
    }

    public boolean isBlacklistedToken(String token) {
        return blacklistedTokenRepository.existsByToken(token);
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

}
