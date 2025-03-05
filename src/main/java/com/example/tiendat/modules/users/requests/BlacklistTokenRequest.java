package com.example.tiendat.modules.users.requests;

import lombok.*;

import jakarta.validation.constraints.NotBlank;

@Data
public class BlacklistTokenRequest {

    @NotBlank(message = "Token khong duoc de trong")
    private String token;
    
    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

}
