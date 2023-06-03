package com.example.communication.whatsapp.persistance.models;

import lombok.Data;

@Data
public class WhatsAppRequest {
    private String messaging_product;
    private boolean preview_url;
    private String recipient_type;
    private String to;
    private String type;
    RequestText text;
}
