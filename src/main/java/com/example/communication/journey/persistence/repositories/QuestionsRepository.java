package com.example.communication.journey.persistence.repositories;

import com.example.communication.journey.persistence.entities.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionsRepository extends JpaRepository<Question,Long> {
}
