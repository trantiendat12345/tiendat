package com.example.tiendat.modules.users.services.interfaces;

import com.example.tiendat.modules.users.requests.LoginRequest;
import com.example.tiendat.modules.users.resources.LoginResource;

public interface UserServiceInterface {
    
    LoginResource login(LoginRequest request);

}
