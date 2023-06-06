package com.example.communication.auth.web.controllers;

import com.example.communication.auth.persistence.entities.Users;
import com.example.communication.auth.persistence.models.AuthRequest;
import com.example.communication.auth.services.UserService;
import com.example.communication.shared.persistance.models.ResponseModel;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
public class UserController {
    @Autowired
    UserService userService;
    @PostMapping("login")
    public ResponseEntity<ResponseModel> login(@RequestBody AuthRequest auth){
        ResponseModel response=userService.login(auth);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping("createUser")
    public ResponseEntity<ResponseModel> createUser(@RequestBody Users user){
        ResponseModel response=userService.createUser(user);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("allUsers")
    public ResponseEntity<ResponseModel> getAllUsers(){
        ResponseModel response=userService.getAllUsers();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping("getUserByName")
    public ResponseEntity<ResponseModel> getUserByName(@RequestParam String name){
        ResponseModel response=userService.getUserByName(name);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping("getUserById")
    public ResponseEntity<ResponseModel> getUserById(@RequestParam int id){
        ResponseModel response=userService.getUserById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
