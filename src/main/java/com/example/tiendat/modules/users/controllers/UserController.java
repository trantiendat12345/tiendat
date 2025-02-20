package com.example.tiendat.modules.users.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.tiendat.modules.users.entities.User;
import com.example.tiendat.resources.SuccessResource;
import com.example.tiendat.modules.users.resources.UserResource;
import com.example.tiendat.modules.users.repositories.UserRepository;

@RestController
@RequestMapping("/api/v1")
public class UserController { // quan li nguoi dung

    @Autowired
    private UserRepository userRepository;
    
    @GetMapping("me")
    public ResponseEntity<?> me() {
        String email = "trantiendat@gmail.com";

        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        UserResource userResource = new UserResource(user.getId(), user.getEmail(), user.getName());

        SuccessResource<UserResource> response = new SuccessResource<>("Success", userResource);

        return ResponseEntity.ok(response);
    }

}
