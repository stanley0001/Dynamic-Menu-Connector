package com.example.communication.journey.services.impl;

import com.example.communication.journey.persistence.entities.Journey;
import com.example.communication.journey.persistence.entities.Question;
import com.example.communication.journey.persistence.entities.UserResponse;
import com.example.communication.journey.persistence.repositories.JourneyRepository;
import com.example.communication.journey.persistence.repositories.QuestionsRepository;
import com.example.communication.journey.persistence.repositories.UserResponseRepository;
import com.example.communication.journey.services.JourneyService;
import com.example.communication.shared.persistance.entities.Sessions;
import com.example.communication.shared.persistance.models.PostEntity;
import com.example.communication.shared.persistance.models.ResponseModel;
import com.example.communication.shared.services.RestEntity;
import com.example.communication.shared.services.SessionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class JourneyServiceImpl implements JourneyService {
    @Autowired
    SessionService sessionService;
    @Autowired
    QuestionsRepository questionsRepository;
    @Autowired
    JourneyRepository journeyRepository;
    @Autowired
    UserResponseRepository userResponseRepository;
    @Autowired
    RestEntity<?> restEntity;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public ResponseModel createQuestion(Question question){
        ResponseModel response=new ResponseModel();
        Optional<Question> preExistingQuestion=questionsRepository.findById(question.getId());
        if (preExistingQuestion.isPresent()){
            response.setStatus("200");
            response.setBody(preExistingQuestion.get());
            response.setMessage("Pre existing question with the same id found");
            return response;
        }
        response.setBody(questionsRepository.save(question));
        response.setStatus("201");
        response.setMessage("Question Created");
        return response;
    }
    public ResponseModel updateQuestion(Question question){
        ResponseModel response=new ResponseModel();
        Optional<Question> preExistingQuestion=questionsRepository.findById(question.getId());
        if (preExistingQuestion.isPresent()){
            response.setStatus("200");
            response.setBody(questionsRepository.save(question));
            response.setMessage("Question updated");
            return response;
        }
        response.setStatus("404");
        response.setMessage("Question not Found");
        return response;
    }
    public ResponseModel getAllQuestions(int page, int limit){
        ResponseModel response=new ResponseModel();
        List<Question> menu=questionsRepository.findAll();
        if (menu.isEmpty()){
            response.setMessage("No Menu found");
            response.setStatus("404");
            return response;
        }
        response.setMessage("Menu found");
        response.setStatus("200");
        response.setBody(menu);
        return response;
    }
    public ResponseModel createJourney(Journey journey){
        ResponseModel response=new ResponseModel();
        Optional<Journey> preExistingJourney=journeyRepository.findById(journey.getId());
        if (preExistingJourney.isPresent()){
            response.setStatus("200");
            response.setBody(preExistingJourney.get());
            response.setMessage("Pre existing journey with the same id found");
            return response;
        }
        response.setBody(journeyRepository.save(journey));
        response.setStatus("200");
        response.setMessage("Journey Created");
        return response;
    }
    public ResponseModel updateJourney(Journey journey){
        ResponseModel response=new ResponseModel();
        Optional<Journey> preExistingJourney=journeyRepository.findById(journey.getId());
        if (preExistingJourney.isPresent()){
            response.setStatus("200");
            response.setBody(journeyRepository.save(journey));
            response.setMessage("Journey updated");
            return response;
        }
        response.setStatus("404");
        response.setMessage("Journey not Found");
        return response;
    }
    public ResponseModel getAllJourney(int page, int limit){
        ResponseModel response=new ResponseModel();
        List<Journey> journey=journeyRepository.findAll();
        if (journey.isEmpty()){
            response.setMessage("No Journey found");
            response.setStatus("404");
            return response;
        }
        response.setMessage("Journey found");
        response.setStatus("200");
        response.setBody(journey);
        return response;
    }
    public Question getQuestionById(Long questionId){
        Optional<Question> question=questionsRepository.findById(questionId);
        if (question.isPresent())
            return question.get();
        return null;
    }
    public Journey getJourneyById(Long questionId){
        Optional<Journey> journey=journeyRepository.findById(questionId);
        if (journey.isPresent())
            return journey.get();
        return null;
    }
    public Journey getJourneyByName(String name){
        Optional<Journey> journey=journeyRepository.findByName(name);
        if (journey.isPresent())
            return journey.get();
        return null;
    }
    public ResponseModel handleRequest(Long sessionId,String text) {
        ResponseModel responseModel=new ResponseModel();
        // Retrieve or create the user's session
        Sessions session = sessionService.findSessionById(sessionId);

        // Check if the session is in the process of answering questions
        if (session.getIsAnsweringQuestions()) {
            // Save the user's response to the current question
            Question currentQuestion = this.getQuestionById(session.getCurrentQuestion());
            UserResponse response = new UserResponse();
            response.setSessionId(session.getId());
            response.setJourney(this.getJourneyById(session.getJourney()));
            response.setQuestion(currentQuestion);
            response.setResponse(text);
            userResponseRepository.save(response);

            // Check if it's the last question
            List<Question> questions = this.getJourneyById(session.getJourney()).getQuestions();
            int currentIndex = questions.indexOf(currentQuestion);
            if (currentIndex == questions.size() - 1) {
                List<UserResponse> userResponses = userResponseRepository.getBySessionId(session.getId());
                Map<String, String> payload = new HashMap<>();
                for (UserResponse currentResponse : userResponses) {
                    String fieldName = currentResponse.getQuestion().getFieldName();
                    String value = currentResponse.getResponse();
                    payload.put(fieldName, value);
                }
                ObjectMapper mapper = new ObjectMapper();
                String jsonPayload=null;
                try {
                    jsonPayload = mapper.writeValueAsString(payload);
                } catch (JsonProcessingException e) {
                    log.error("Error forming json payload: {}",e.getMessage());
                }
                //http request to submit data
                log.info("formed json payload {}",jsonPayload);
                PostEntity postEntity=new PostEntity();
                postEntity.setUrl(this.getJourneyById(this.getJourneyById(session.getJourney()).getId()).getSubmissionUrl());
                postEntity.setEndpoint("");
                // Set the request headers
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                postEntity.setHeaders(headers);
                postEntity.setBody(jsonPayload);
                restEntity.post(postEntity);
                session.setIsAnsweringQuestions(false);
                sessionService.updateSession(session);
                responseModel.setStatus("301");
                responseModel.setMessage("Thank you!");
                return responseModel;
            } else {
                // Move to the next question
                Question nextQuestion = questions.get(currentIndex + 1);
                session.setCurrentQuestion(nextQuestion.getId());
                sessionService.updateSession(session);
                responseModel.setStatus("301");
                responseModel.setMessage(nextQuestion.getText());
                return responseModel;
            }
        }else {
            // Not currently answering questions, expect journey name
            Journey journey = this.getJourneyByName(text);
            if (journey != null) {
                Question currentQuestion=journey.getQuestions().get(0);
                session.setJourney(journey.getId());
                session.setIsAnsweringQuestions(true);
                session.setCurrentQuestion(currentQuestion.getId());
                sessionService.updateSession(session);
                responseModel.setStatus("301");
                log.info("question: {}",currentQuestion);
                responseModel.setMessage(currentQuestion.getText());
                return responseModel;
            }
        }
        responseModel.setStatus("301");
        responseModel.setMessage("00.Home \n000.Logout");
        return responseModel;
    }
}
