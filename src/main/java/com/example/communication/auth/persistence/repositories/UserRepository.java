package com.example.communication.auth.persistence.repositories;

import com.example.communication.auth.persistence.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users,Long> {
}
