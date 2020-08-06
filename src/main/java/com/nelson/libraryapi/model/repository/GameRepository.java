package com.nelson.libraryapi.model.repository;

import com.nelson.libraryapi.model.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {

    boolean existsByCode(String code);
}
