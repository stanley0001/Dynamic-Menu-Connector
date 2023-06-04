package com.example.communication.journey.persistence.repositories;

import com.example.communication.journey.persistence.entities.UserResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserResponseRepository extends JpaRepository<UserResponse,Long> {
    List<UserResponse> getBySessionId(Long id);
}
