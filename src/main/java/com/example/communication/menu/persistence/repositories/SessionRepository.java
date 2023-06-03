package com.example.communication.menu.persistence.repositories;

import com.example.communication.menu.persistence.entities.Sessions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SessionRepository extends JpaRepository<Sessions,Long> {
    Optional<Sessions> getTopByPhone(String phone);

    Optional<Sessions> findTopByPhone(String phone);

    Optional<Sessions> findByPhoneOrderByUpdatedAtDesc(String phone);

    Optional<Sessions> findFirstByPhoneOrderByUpdatedAtDesc(String phone);
}
