package com.example.tiendat.modules.users.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.tiendat.modules.users.requests.LoginRequest;
import com.example.tiendat.modules.users.resources.LoginResource;
import com.example.tiendat.modules.users.services.interfaces.UserServiceInterface;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserServiceInterface userService;

    public AuthController(UserServiceInterface userService) {
        this.userService = userService;
    }

    @PostMapping("login")
    public ResponseEntity<LoginResource> login(@RequestBody LoginRequest request) {
        LoginResource auth = userService.login(request);
        return ResponseEntity.ok(auth);
    }
    
}
