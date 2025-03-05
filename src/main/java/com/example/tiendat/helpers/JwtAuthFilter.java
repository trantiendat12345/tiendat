package com.example.tiendat.helpers;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import lombok.RequiredArgsConstructor;

import com.example.tiendat.modules.users.services.impl.CustomUserDetailsService;
import com.example.tiendat.services.JWTService;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;
import jakarta.validation.constraints.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
// @RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter{

    private final JWTService jwtService;
    private final CustomUserDetailsService customUserDetailsService;
    private final ObjectMapper objectMapper;

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);

    public JwtAuthFilter(JWTService jwtService, CustomUserDetailsService customUserDetailsService, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected boolean shouldNotFilter( // kiem tra neu la route login thi khong filter
        @NonNull HttpServletRequest request
    ) {
        String path = request.getRequestURI();
        return path.startsWith("/api/v1/auth/login");
    }

    @Override
    public void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        try {

            final String authHeader = request.getHeader("Authorization");
            final String jwt;
            final String userId;

            if(authHeader == null || !authHeader.startsWith("Bearer ")) {
                // logger.error("Token missing");

                sendErrorResponse(
                    response,
                    request, HttpServletResponse.SC_UNAUTHORIZED,
                    "XAC THUC KHONG THANH CONG",
                    "KHONG TIM THAY TOKEN"
                );

                // filterChain.doFilter(request, response);

                return;
            }

            jwt = authHeader.substring(7); // remove "Bearer " from the token

            if(!jwtService.isTokenFormatValid(jwt)) {

                sendErrorResponse(
                    response,
                    request,
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "XAC THUC KHONG THANH CONG",
                    "TOKEN KHONG DUNG DINH DANG"
                );

                return;
            }

            if(!jwtService.isSignatureValid(jwt)) {

                sendErrorResponse(
                    response,
                    request,
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "XAC THUC KHONG THANH CONG",
                    "TOKEN KHONG DUNG CHU KY"
                );

                return;
            }

            if(!jwtService.isTokenExpired(jwt)) {

                sendErrorResponse(
                    response,
                    request,
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "XAC THUC KHONG THANH CONG",
                    "TOKEN HET HAN"
                );

                return;
            }

            if(!jwtService.isIssuerToken(jwt)) {

                sendErrorResponse(
                    response,
                    request,
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "XAC THUC KHONG THANH CONG",
                    "TOKEN KHONG HOP LE"
                );

                return;
            }

            if(jwtService.isBlacklistedToken(jwt)) {

                sendErrorResponse(
                    response,
                    request,
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "XAC THUC KHONG THANH CONG",
                    "TOKEN BI KHOA"
                );

                return;

            }

            userId = jwtService.getUserIdFromJwt(jwt);
            // logger.info(userId);
            if(userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(userId);

                final String emailFromToken = jwtService.getEmailFromJwt(jwt);
                if(!emailFromToken.equals(userDetails.getUsername())) {
                    sendErrorResponse(
                        response,
                        request,
                        HttpServletResponse.SC_UNAUTHORIZED,
                        "XAC THUC KHONG THANH CONG",
                        "TOKEN KHONG CHINH XAC"
                    );
                    return;
                }

                // logger.info("userDetails: " + userDetails.getUsername());

                // if(!jwtService.isTokenFormatValid(jwt)) {
                //     sendErrorResponse(response, request, HttpServletResponse.SC_UNAUTHORIZED, "xac thuc khong thanh cong", "Token khong dung dinh dang");
                //     return;
                // }

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails, 
                    null, 
                    userDetails.getAuthorities()
                );

                authToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authToken);
                logger.info("xac thuc tai khoan thanh cong" + userDetails.getUsername());

                // logger.info(userDetails.getUsername());
            }

            filterChain.doFilter(request, response);
            
        } catch (ServletException | IOException e) {

            sendErrorResponse(
                response,
                request,
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                "Network Error!",
                e.getMessage()
            );            
        }
        
    }

    private void sendErrorResponse(
        @NotNull HttpServletResponse response,
        @NotNull HttpServletRequest request,
        int statusCode,
        String error, String message
    ) throws IOException {
        response.setStatus(statusCode);
        response.setContentType("application/json;charset=UTF-8");

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", System.currentTimeMillis());
        errorResponse.put("status", statusCode);
        errorResponse.put("error", error);
        errorResponse.put("message", message);
        errorResponse.put("path", request.getRequestURI());

        String jsonResponse = objectMapper.writeValueAsString(errorResponse);

        response.getWriter().write(jsonResponse);
    }
    
}
