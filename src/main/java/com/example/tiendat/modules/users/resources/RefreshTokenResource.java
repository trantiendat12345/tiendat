package com.example.tiendat.modules.users.resources;

import lombok.*;

@Data
@RequiredArgsConstructor
// @AllArgsConstructor
public class RefreshTokenResource {
    
    private String token;
    private String refreshToken;

    public RefreshTokenResource(String token, String refreshToken) {
        this.token = token;
        this.refreshToken = refreshToken;
    }

}
