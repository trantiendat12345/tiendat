package com.example.tiendat.modules.users.services.impl;

import com.example.tiendat.modules.users.requests.LoginRequest;
import com.example.tiendat.modules.users.resources.LoginResource;
import com.example.tiendat.modules.users.resources.UserResource;
import com.example.tiendat.modules.users.services.interfaces.UserServiceInterface;
import com.example.tiendat.services.BaseService;

import org.springframework.stereotype.Service;

@Service
public class UserService extends BaseService implements UserServiceInterface {

    @Override
    public LoginResource login(LoginRequest request) {
        try {

            // String email = request.getEmail();
            // String password = request.getPassword();

            String token = "random_token";
            UserResource user = new UserResource(1L, "trantiendat@gmail.com");

            return new LoginResource(token, user);

        } catch (Exception e) {

            throw new RuntimeException("Error");

        }
    
    }

}
