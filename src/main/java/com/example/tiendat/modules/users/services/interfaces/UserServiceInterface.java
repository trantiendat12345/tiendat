package com.example.tiendat.modules.users.services.interfaces;

import com.example.tiendat.modules.users.requests.LoginRequest;

public interface UserServiceInterface {
    
    Object authenticate(LoginRequest request);

}
