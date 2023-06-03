package com.example.communication.whatsapp.persistance.models;

import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
@Data
@ToString
public class WebHook{
    public String object;
    public ArrayList<Entry> entry;
}


