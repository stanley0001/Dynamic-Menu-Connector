package com.example.communication.journey.services;

import com.example.communication.journey.persistence.entities.Journey;
import com.example.communication.journey.persistence.entities.Question;
import com.example.communication.shared.persistance.models.ResponseModel;
import org.springframework.stereotype.Service;

@Service
public interface JourneyService {
    ResponseModel getAllQuestions(int page, int limit);
    ResponseModel updateQuestion(Question question);
    ResponseModel createQuestion(Question question);
    ResponseModel getAllJourney(int page, int limit);
    ResponseModel updateJourney(Journey journey);
    ResponseModel createJourney(Journey journey);
    ResponseModel handleRequest(Long sessionId,String text);
}
