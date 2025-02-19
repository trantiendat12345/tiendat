package com.example.tiendat.modules.users.requests;

import jakarta.validation.constraints.Email; // dung de kiem tra xem co phai la email khong
import jakarta.validation.constraints.NotBlank; // dung de kiem tra xem co rong khong
import jakarta.validation.constraints.Size; // dung de kiem tra xem co dung kich thuoc khong

public class LoginRequest {
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email is not valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
