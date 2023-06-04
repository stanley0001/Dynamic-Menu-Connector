package com.example.communication.shared.web.controllers;

import com.example.communication.shared.persistance.entities.Sessions;
import com.example.communication.shared.persistance.models.ResponseModel;
import com.example.communication.shared.services.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("session")
public class SessionController {
    @Autowired
    SessionService sessionService;
    @PostMapping("createSession")
    public ResponseEntity<ResponseModel> createSession(@RequestBody Sessions session){
        ResponseModel response=sessionService.createSession(session);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("getRecentActiveSession")
    public ResponseEntity<ResponseModel> getRecentActiveSession(@RequestParam String phone){
        ResponseModel response=sessionService.getMostRecentSession(phone);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
