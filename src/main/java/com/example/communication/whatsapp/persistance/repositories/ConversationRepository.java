package com.example.communication.whatsapp.persistance.repositories;

import com.example.communication.whatsapp.persistance.entities.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationRepository extends JpaRepository<Conversation,Long> {
}
