package com.example.communication.whatsapp.persistance.models;

import lombok.Data;

@Data
public class Status {
    public String id;
    public String status;
    public String timestamp;
    public String recipient_id;
    public Conversation conversation;
    public Pricing pricing;
}
