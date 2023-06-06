package com.example.communication.auth.persistence.models;

import lombok.Data;

@Data
public class AuthResponse {
    private String username;
    private String token;
    private String refreshToken;
    private String tokenExpiry;

}
