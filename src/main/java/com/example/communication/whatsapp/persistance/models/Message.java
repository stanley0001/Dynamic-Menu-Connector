package com.example.communication.whatsapp.persistance.models;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Message {
    public String from;
    public String id;
    public String timestamp;
    public Text text;
    public String type;
}
