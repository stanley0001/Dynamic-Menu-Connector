package com.example.communication.journey.web.controllers;

import com.example.communication.journey.persistence.entities.Journey;
import com.example.communication.journey.persistence.entities.Question;
import com.example.communication.journey.services.JourneyService;
import com.example.communication.menu.persistence.entities.Menus;
import com.example.communication.menu.persistence.entities.Options;
import com.example.communication.shared.persistance.models.ResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("journey")
public class JourneyController {
    @Autowired
    JourneyService journeyService;

    @GetMapping("getAllQuestions")
    public ResponseEntity<ResponseModel> getAllQuestions(@RequestParam int page, @RequestParam int limit){
        ResponseModel response=journeyService.getAllQuestions(page,limit);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping("createQuestion")
    public ResponseEntity<ResponseModel> createQuestion(@RequestBody Question question){
        ResponseModel response=journeyService.createQuestion(question);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PutMapping("updateQuestion")
    public ResponseEntity<ResponseModel> updateQuestion(@RequestBody Question question){
        ResponseModel response=journeyService.updateQuestion(question);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("customerJourney")
    public ResponseEntity<ResponseModel> getAllJourney(@RequestParam int page, @RequestParam int limit){
        ResponseModel response=journeyService.getAllJourney(page,limit);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("createJourney")
    public ResponseEntity<ResponseModel> createJourney(@RequestBody Journey journey){
        ResponseModel response=journeyService.createJourney(journey);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PutMapping("updateJourney")
    public ResponseEntity<ResponseModel> updateJourney(@RequestBody Journey journey){
        ResponseModel response=journeyService.updateJourney(journey);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
