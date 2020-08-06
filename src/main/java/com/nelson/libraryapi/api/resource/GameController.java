package com.nelson.libraryapi.api.resource;

import com.nelson.libraryapi.api.dto.GameDTO;
import com.nelson.libraryapi.api.exception.ApiErrors;
import com.nelson.libraryapi.exception.BusinessException;
import com.nelson.libraryapi.model.entity.Game;
import com.nelson.libraryapi.service.GameService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/games")
public class GameController {

    private GameService gameService;

    private ModelMapper modelMapper;

    public GameController(GameService gameService, ModelMapper modelMapper) {
        this.gameService = gameService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GameDTO create(@RequestBody @Valid GameDTO dto) {

        Game entity = modelMapper.map(dto, Game.class);

        entity = gameService.save(entity);

        return modelMapper.map(entity, GameDTO.class);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleValidationExceptions(MethodArgumentNotValidException exc) {
        BindingResult bindingResult = exc.getBindingResult();

        return new ApiErrors(bindingResult);
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleBusinessException(BusinessException exc) {
        return new ApiErrors(exc);
    }

}
