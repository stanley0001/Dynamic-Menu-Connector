package com.example.communication.whatsapp.persistance.models;

import lombok.Data;

@Data
public class Pricing {
    public boolean billable;
    public String pricing_model;
    public String category;
}
