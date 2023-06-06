package com.example.communication.journey.persistence.repositories;

import com.example.communication.journey.persistence.entities.Journey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JourneyRepository extends JpaRepository<Journey,Long> {
    Optional<Journey> findByName(String text);
}
