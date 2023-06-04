package com.example.communication.shared.services;

import com.example.communication.shared.persistance.entities.Sessions;
import com.example.communication.shared.persistance.models.ResponseModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface SessionService {
    ResponseModel createSession(Sessions session);
    ResponseModel updateSession(Sessions session);
    ResponseModel getMostRecentSession(String phone);

    ResponseModel closeSession(Sessions session);

    Optional<Sessions> findSessionById(Long sessionId);
}
