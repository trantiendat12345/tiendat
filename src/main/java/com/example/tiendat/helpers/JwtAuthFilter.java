package com.example.tiendat.helpers;

import org.springframework.stereotype.Component; //
import org.springframework.web.filter.OncePerRequestFilter; //
import org.slf4j.Logger;//
import org.slf4j.LoggerFactory;//
import org.springframework.security.core.context.SecurityContextHolder;//
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.lang.NonNull;//

// import lombok.RequiredArgsConstructor;//

import com.example.tiendat.modules.users.services.impl.CustomUserDetailsService;
import com.example.tiendat.services.JWTService; //
import com.example.tiendat.modules.users.services.impl.UserService;//

import jakarta.servlet.FilterChain;//
import jakarta.servlet.http.HttpServletRequest;//
import jakarta.servlet.http.HttpServletResponse;//
import jakarta.servlet.ServletException;//

import java.io.IOException; //

@Component
// @RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter{

    private final JWTService jwtService;
    private final CustomUserDetailsService customUserDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);

    public JwtAuthFilter(JWTService jwtService, CustomUserDetailsService customUserDetailsService) {
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    public void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userId;

        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.error("Token missing");
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7); // remove "Bearer " from the token
        userId = jwtService.getUserIdFromJwt(jwt);
        // logger.info(userId);
        if(userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(userId);
            logger.info(userDetails.getUsername());
        }

        filterChain.doFilter(request, response);
    }
    
}
