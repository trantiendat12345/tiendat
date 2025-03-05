package com.example.tiendat.modules.users.controllers;

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
import com.example.tiendat.modules.users.resources.LoginResource;
import com.example.tiendat.modules.users.services.interfaces.UserServiceInterface;
import com.example.tiendat.resources.ErrorResource;
import com.example.tiendat.modules.users.requests.BlacklistTokenRequest;
import com.example.tiendat.modules.users.services.impl.BlacklistService;
import com.example.tiendat.resources.MessageResource;

@Validated
@RestController
@RequestMapping("api/v1/auth")
public class AuthController { // quan li dang nhap

    private final UserServiceInterface userService;

    @Autowired
    private BlacklistService blacklistService;

    public static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    public AuthController(UserServiceInterface userService) {
        this.userService = userService;
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        Object result = userService.authenticate(request);
        
        if(result instanceof LoginResource loginResource) {
            return ResponseEntity.ok(loginResource);
        }

        if (result instanceof ErrorResource errorResource) {
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

            Object message = blacklistService.create(request);
            return ResponseEntity.ok(message);

        } catch (Exception e) {

            return ResponseEntity.internalServerError().body(new MessageResource("Network Error!"));

        }

    }
    
    
}
