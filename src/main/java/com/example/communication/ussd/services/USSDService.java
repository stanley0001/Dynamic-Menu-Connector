package com.example.communication.ussd.services;

public interface USSDService {
    String webHook(String networkCode, String phoneNumber, String serviceCode, String sessionId, String text);
}
