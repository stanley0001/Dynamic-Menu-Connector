package com.example.communication.shared.persistance.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Entity
@Data
public class Sessions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String phone;
    private String previousMenu;
    private String ussdSessionId;
    private Boolean active;

    private String currentMenu;
    private Boolean isAnsweringQuestions;
    private Long journey;
    private Long currentQuestion;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

}
