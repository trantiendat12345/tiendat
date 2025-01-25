package com.example.tiendat.modules.users.services.impl;

import com.example.tiendat.modules.users.dtos.LoginRequest;
import com.example.tiendat.modules.users.dtos.LoginResponse;
import com.example.tiendat.modules.users.dtos.UserDTO;
import com.example.tiendat.modules.users.services.interfaces.UserServiceInterface;
import com.example.tiendat.services.BaseService;

import org.springframework.stereotype.Service;

@Service
public class UserService extends BaseService implements UserServiceInterface {

    @Override
    public LoginResponse login(LoginRequest request) {
        try {

            // String email = request.getEmail();
            // String password = request.getPassword();

            String token = "random_token";
            UserDTO user = new UserDTO(1L, "trantiendat@gmail.com");

            return new LoginResponse(token, user);

        } catch (Exception e) {

            throw new RuntimeException("Error");

        }
    
    }

}
