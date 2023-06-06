package com.example.communication.shared.services;

import com.example.communication.shared.persistance.models.PostEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface RestEntity<T> {
    ResponseEntity<T> post(PostEntity postEntity);
}
