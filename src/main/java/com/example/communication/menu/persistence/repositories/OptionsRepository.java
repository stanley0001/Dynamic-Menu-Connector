package com.example.communication.menu.persistence.repositories;

import com.example.communication.menu.persistence.entities.Menus;
import com.example.communication.menu.persistence.entities.Options;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OptionsRepository extends JpaRepository<Options,Long> {
    @Query(
            value = "select * from options where menu = ?1 and option = ?2 limit 1",
            nativeQuery = true
    )
    Optional<Options> findByMenuAndOption(String menu, String option);
    @Query(
            value = "SELECT * FROM options WHERE menu = ?1 AND LOWER(name) LIKE CONCAT('%', LOWER(?2), '%') LIMIT 1",
            nativeQuery = true
    )
    Optional<Options> findByMenuAndText(String menu, String option);
}
