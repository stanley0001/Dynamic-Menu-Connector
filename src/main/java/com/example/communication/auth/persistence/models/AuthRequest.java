package com.example.communication.auth.persistence.models;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;
    private String otp;
}
