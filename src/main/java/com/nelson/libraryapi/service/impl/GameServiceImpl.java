package com.nelson.libraryapi.service.impl;

import com.nelson.libraryapi.exception.BusinessException;
import com.nelson.libraryapi.model.entity.Game;
import com.nelson.libraryapi.model.repository.GameRepository;
import com.nelson.libraryapi.service.GameService;
import org.springframework.stereotype.Service;

@Service
public class GameServiceImpl implements GameService {

    private GameRepository repository;

    public GameServiceImpl(GameRepository repository) {
        this.repository = repository;
    }

    @Override
    public Game save(Game game) {
        if (repository.existsByCode(game.getCode())) {
            throw new BusinessException("Code já cadastrado");
        }
        return repository.save(game);
    }
}
