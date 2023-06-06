package com.example.communication.ussd.web.controllers;

import com.example.communication.ussd.services.USSDService;
import com.example.communication.whatsapp.services.MetaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("ussd")
public class UssdController {
    @Autowired
    USSDService ussdService;
    @PostMapping("webhook")
    public ResponseEntity<String> webhook(@RequestParam("networkCode") String networkCode,@RequestParam("phoneNumber") String phoneNumber,@RequestParam("serviceCode") String serviceCode,@RequestParam("sessionId") String sessionId,@RequestParam("text") String text){
        String response=ussdService.webHook(networkCode,phoneNumber,serviceCode,sessionId,text);
        return new ResponseEntity(response, HttpStatus.OK);
    }
}
