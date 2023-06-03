package com.example.communication.whatsapp.persistance.models;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Change {
    public Value value;
    public String field;
}
