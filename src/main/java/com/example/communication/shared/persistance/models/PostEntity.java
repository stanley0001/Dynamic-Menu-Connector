package com.example.communication.shared.persistance.models;

import lombok.Data;
import org.springframework.http.HttpHeaders;

@Data
public class PostEntity<T> {
    private String url;
    private String endpoint;
    HttpHeaders headers;
    private T body;
}
