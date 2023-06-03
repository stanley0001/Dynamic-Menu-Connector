package com.example.communication.whatsapp.persistance.models;

import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;

@Data
@ToString
public class Entry {
    public String id;
    public ArrayList<Change> changes;
}
