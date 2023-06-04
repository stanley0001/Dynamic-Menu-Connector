package com.example.communication.shared.persistance.models;

import lombok.Data;

@Data
public class ResponseModel<T> {
    private String status;
    private String message;
    private T body;
}
