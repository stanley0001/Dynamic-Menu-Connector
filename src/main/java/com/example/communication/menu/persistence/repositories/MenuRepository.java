package com.example.communication.menu.persistence.repositories;

import com.example.communication.menu.persistence.entities.Menus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menus,Long> {

    Optional<Menus> findTopByLevel(String aDefault);

    Optional<Menus> findByName(String name);

    Optional<Menus> findTopByName(String name);
}
