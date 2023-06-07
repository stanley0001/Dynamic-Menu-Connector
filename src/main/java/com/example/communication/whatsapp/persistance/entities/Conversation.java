package com.example.communication.whatsapp.persistance.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String externalId;
    private String conversationId;
    private String phone;
    private String name;
    private String message;
    private Boolean response;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public Conversation() {
    }

    public Conversation(String name, String phone, String messageId, String conversationId, String message, boolean response) {
        this.name = name;
        this.phone= phone;
        this.message = message;
        this.conversationId= conversationId;
        this.externalId = messageId;
        this.response = response;
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
