package com.example.communication.whatsapp.persistance.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;

@Data
@ToString
public class Value {
    public String messaging_product;
    public Metadata metadata;
    @JsonIgnoreProperties(ignoreUnknown = true)
    public ArrayList<Contact> contacts;
    @JsonIgnoreProperties(ignoreUnknown = true)
    public ArrayList<Status> statuses;
    @JsonIgnoreProperties(ignoreUnknown = true)
    public ArrayList<Message> messages;
}
