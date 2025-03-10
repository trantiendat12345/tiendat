package com.example.tiendat.modules.users.controllers;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.validation.Valid;

import com.example.tiendat.modules.users.requests.LoginRequest;
import com.example.tiendat.modules.users.requests.RefreshTokenRequest;
import com.example.tiendat.modules.users.resources.LoginResource;
import com.example.tiendat.modules.users.resources.RefreshTokenResource;
import com.example.tiendat.modules.users.services.interfaces.UserServiceInterface;
import com.example.tiendat.resources.ErrorResource;
import com.example.tiendat.modules.users.entities.RefreshToken;
import com.example.tiendat.modules.users.repositories.RefreshTokenRepository;
import com.example.tiendat.modules.users.requests.BlacklistTokenRequest;
import com.example.tiendat.modules.users.services.impl.BlacklistService;
import com.example.tiendat.resources.MessageResource;
import com.example.tiendat.services.JWTService;
import com.example.tiendat.resources.ApiResource;

@Validated
@RestController
@RequestMapping("api/v1/auth")
public class AuthController { // quan li dang nhap

    private final UserServiceInterface userService;
    public static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private BlacklistService blacklistService;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    public AuthController(UserServiceInterface userService) {
        this.userService = userService;
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        Object result = userService.authenticate(request);
        
        if(result instanceof LoginResource loginResource) {

            ApiResource<LoginResource> response = ApiResource.ok(loginResource, "SUCCESS");

            return ResponseEntity.ok(response);
        }

        if (result instanceof ApiResource errorResource) {
            return ResponseEntity.unprocessableEntity().body(errorResource);
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Network error");
    }

    @PostMapping("blacklisted_tokens")
    public ResponseEntity<?> addTokenToBlacklist(@Valid @RequestBody BlacklistTokenRequest request) {
        
        try {

            logger.info("1234");
            
            Object result = blacklistService.create(request);

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            
            return ResponseEntity.internalServerError().body(new MessageResource("Network Error!") );

        }

    }

    @GetMapping("loggout")
    public ResponseEntity<?> loggout(@RequestHeader("Authorization") String bearerToken) {

        try {
            
            String token = bearerToken.substring(7);

            BlacklistTokenRequest request = new BlacklistTokenRequest();
            request.setToken(token);
            blacklistService.create(request);

            // Object message = blacklistService.create(request);

            ApiResource<Void> response = ApiResource.<Void>builder()
                .success(true)
                .message("DANG XUAT THANH CONG")
                .status(HttpStatus.OK)
                .build();

            return ResponseEntity.ok(response);

        } catch (Exception e) {

            ApiResource<Void> errorResponse = ApiResource.<Void>builder()
                .success(false)
                .message("Network Error!")
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();

            return ResponseEntity.internalServerError().body(errorResponse);

        }

    }

    @PostMapping("refresh")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {

        String refreshToken = request.getRefreshToken();

        if(!jwtService.isRefreshTokenValid(refreshToken)) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResource("Refresh Token khong hop le"));

        }

        Optional<RefreshToken> dbRefreshTokenOptional = refreshTokenRepository.findByRefreshToken(refreshToken);

        if(dbRefreshTokenOptional.isPresent()) {

            RefreshToken dBRefreshToken = dbRefreshTokenOptional.get();
            Long userId = dBRefreshToken.getUserId();
            String email = dBRefreshToken.getUser().getEmail();

            String newToken = jwtService.generateToken(userId, email, null);
            String newRefreshToken = jwtService.generateRefreshToken(userId, email);

            return ResponseEntity.ok(new RefreshTokenResource(newToken, newRefreshToken));
            
        }

        return ResponseEntity.internalServerError().body(new MessageResource("Network Error!"));

        // Long userId = Long.valueOf(jwtService.getUserIdFromJwt(refreshToken));
        // String email = jwtService.getEmailFromJwt(refreshToken);

        // String newToken = jwtService.generateToken(userId, email);
        // String newRefreshToken = jwtService.generateRefreshToken(userId, email);

        // return ResponseEntity.ok(new RefreshTokenResource(newToken, newRefreshToken));

    }
    
    
    
}
