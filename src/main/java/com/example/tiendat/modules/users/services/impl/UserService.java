package com.example.tiendat.modules.users.services.impl;

import com.example.tiendat.modules.users.entities.User;
import com.example.tiendat.modules.users.repositories.UserRepository;
import com.example.tiendat.modules.users.requests.LoginRequest;
import com.example.tiendat.modules.users.resources.LoginResource;
import com.example.tiendat.modules.users.resources.UserResource;
import com.example.tiendat.modules.users.services.interfaces.UserServiceInterface;
import com.example.tiendat.resources.ErrorResource;
import com.example.tiendat.services.BaseService;
import com.example.tiendat.services.JWTService;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService extends BaseService implements UserServiceInterface {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private JWTService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;


    @Override
    public Object authenticate(LoginRequest request) {
        try {

            // String email = request.getEmail();
            // String password = request.getPassword();

            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new BadCredentialsException("email or password is incorrect"));

            if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                throw new BadCredentialsException("email or password is incorrect");
            }

            // String token = "random_token";
            UserResource userResource = new UserResource(user.getId(), user.getEmail(), user.getName(), user.getPhone());
            String token = jwtService.generateToken(user.getId(), user.getEmail()); // token chua thong tin user

            String refreshToken = jwtService.generateRefreshToken(user.getId(), user.getEmail());

            return new LoginResource(token,refreshToken, userResource);

        } catch (BadCredentialsException e) {

            // throw new RuntimeException("Error " + e.getMessage());
            logger.error("Error: ", e.getMessage());

            Map<String, String> errors = new HashMap<>();
            errors.put("message", e.getMessage());
            ErrorResource errorResource = new ErrorResource("Error",errors);
            return errorResource;

        }
    
    }

}
