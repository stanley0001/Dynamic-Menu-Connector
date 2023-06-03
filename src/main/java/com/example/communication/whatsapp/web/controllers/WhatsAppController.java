package com.example.communication.whatsapp.web.controllers;

import com.example.communication.whatsapp.services.MetaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("whatsapp")
public class WhatsAppController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    MetaService metaService;
    @GetMapping("webhook")
    public ResponseEntity<String> getWebhook(@RequestParam("hub.mode") String mode,@RequestParam("hub.challenge") String challenge,@RequestParam("hub.verify_token") String token){
        return new ResponseEntity(challenge, HttpStatus.OK);
    }
    @PostMapping("webhook")
    public ResponseEntity<ResponseEntity> postWebhook(@RequestBody String message){
        metaService.webHook(message);
        return new ResponseEntity("Ok", HttpStatus.OK);
    }
}
