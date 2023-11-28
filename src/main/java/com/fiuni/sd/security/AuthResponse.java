package com.fiuni.sd.security;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponse {
    private String email;
    private String accessToken;

    public AuthResponse() {
        throw new IllegalArgumentException("User email and access token are required");
    }

    public AuthResponse(String email, String accessToken) {
        this.email = email;
        this.accessToken = accessToken;
    }
}
