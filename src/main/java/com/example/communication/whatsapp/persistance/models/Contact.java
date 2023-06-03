package com.example.communication.whatsapp.persistance.models;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Contact {
    public Profile profile;
    public String wa_id;
}
