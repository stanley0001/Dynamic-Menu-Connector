package com.example.communication.shared.services.impl;

import com.example.communication.shared.persistance.entities.Sessions;
import com.example.communication.shared.persistance.models.ResponseModel;
import com.example.communication.shared.persistance.repositories.SessionRepository;
import com.example.communication.shared.services.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SessionServiceImpl implements SessionService {
    private static final int sessionExpiry=2;

    @Autowired
    SessionRepository sessionRepository;
    public ResponseModel createSession(Sessions session){
        ResponseModel response=new ResponseModel();
        session.setIsAnsweringQuestions(false);
        session.setCurrentMenu("WELCOME");
        session.setActive(true);
        response.setStatus("201");
        response.setMessage("Session Created");
        response.setBody(sessionRepository.save(session));
        return response;
    }
    public ResponseModel updateSession(Sessions session){
        session.setUpdatedAt(LocalDateTime.now());
        ResponseModel response=new ResponseModel();
        response.setStatus("201");
        response.setMessage("Session Created");
        response.setBody(sessionRepository.save(session));
        return response;
    }
    public Sessions findSessionById(Long sessionId){
        return sessionRepository.findById(sessionId).get();
    }
    public ResponseModel getMostRecentSession(String phone){
        ResponseModel response=new ResponseModel();
        Optional<Sessions> session=sessionRepository.findFirstByPhoneOrderByUpdatedAtDesc(phone);
        if (session.isPresent()){
            if (session.get().getUpdatedAt().isAfter(LocalDateTime.now().minusHours(sessionExpiry)) && session.get().getActive()){
                response.setStatus("200");
                response.setMessage("Active Session Found");
                response.setBody(session.get());
                return response;
            }
        }
        response.setStatus("404");
        response.setMessage("No Active Session Found");
        return response;
    }
    public ResponseModel closeSession(Sessions session){
        ResponseModel response=new ResponseModel();
        session.setActive(false);
        response.setStatus("301");
        response.setMessage("Session Closed");
        session.setUpdatedAt(LocalDateTime.now());
        sessionRepository.save(session);
        response.setMessage("Bye.. \n Welcome Back!");
        return response;
    }
}
