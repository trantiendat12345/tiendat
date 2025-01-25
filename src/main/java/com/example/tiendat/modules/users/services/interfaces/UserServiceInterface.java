package com.example.tiendat.modules.users.services.interfaces;

import com.example.tiendat.modules.users.dtos.LoginResponse;
import com.example.tiendat.modules.users.dtos.LoginRequest;

public interface UserServiceInterface {
    
    LoginResponse login(LoginRequest request);

}
