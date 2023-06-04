package com.example.communication.journey.persistence.repositories;

import com.example.communication.journey.persistence.entities.Journey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JourneyRepository extends JpaRepository<Journey,Long> {
    Journey findByName(String text);
}
