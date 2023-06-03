package com.example.communication.whatsapp.persistance.models;

import lombok.Data;

@Data
public class Conversation {
    public String id;
    public String expiration_timestamp;
    public Origin origin;
}
