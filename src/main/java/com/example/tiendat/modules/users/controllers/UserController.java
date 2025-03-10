package com.example.tiendat.modules.users.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import com.example.tiendat.modules.users.entities.User;
// import com.example.tiendat.resources.SuccessResource;
import com.example.tiendat.modules.users.resources.UserResource;
import com.example.tiendat.modules.users.repositories.UserRepository;
import com.example.tiendat.resources.ApiResource;

@RestController
@RequestMapping("/api/v1")
public class UserController { // quan li nguoi dung

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    
    @GetMapping("me")
    public ResponseEntity<?> me() {
        // String email = "trantiendat@gmail.com";

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        logger.info(email);

        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        // UserResource userResource = new UserResource(user.getId(), user.getEmail(), user.getName());

        UserResource userResource = UserResource.builder()
            .id(user.getId())
            .email(user.getEmail())
            .name(user.getName())
            .phone(user.getPhone())
            .build();

        ApiResource<UserResource> response = ApiResource.ok(userResource, "SUCCESS");

        // SuccessResource<UserResource> response = new SuccessResource<>("Success", userResource);
        logger.info("Success!");

        return ResponseEntity.ok(response);
    }

}
