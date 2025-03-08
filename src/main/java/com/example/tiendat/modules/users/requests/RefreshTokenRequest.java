package com.example.tiendat.modules.users.requests;

import lombok.*;

import jakarta.validation.constraints.NotBlank;

@Data
public class RefreshTokenRequest {

    @NotBlank(message = "RefreshToken khong duoc de trong")
    private String refreshToken;
    
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

}
