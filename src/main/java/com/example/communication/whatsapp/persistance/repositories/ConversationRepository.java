package com.example.communication.whatsapp.persistance.repositories;

import com.example.communication.whatsapp.persistance.entities.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation,Long> {
}
