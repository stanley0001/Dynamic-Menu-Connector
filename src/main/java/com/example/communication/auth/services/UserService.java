package com.example.communication.auth.services;

import com.example.communication.auth.persistence.entities.Users;
import com.example.communication.auth.persistence.models.AuthRequest;
import com.example.communication.shared.persistance.models.ResponseModel;

public interface UserService {
    ResponseModel createUser(Users user);
    ResponseModel login(AuthRequest auth);
    ResponseModel getAllUsers();
    ResponseModel getUserByName(String name);
    ResponseModel getUserById(int id);
}
