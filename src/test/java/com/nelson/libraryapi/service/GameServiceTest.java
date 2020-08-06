package com.nelson.libraryapi.service;

import com.nelson.libraryapi.exception.BusinessException;
import com.nelson.libraryapi.model.entity.Game;
import com.nelson.libraryapi.model.repository.GameRepository;
import com.nelson.libraryapi.service.impl.GameServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class GameServiceTest {

    GameService gameService;

    @MockBean
    GameRepository repository;

    @BeforeEach
    public void setUp() {
        this.gameService = new GameServiceImpl(repository);
    }

    @Test
    @DisplayName("Deve salvar um Game")
    public void saveGameTest() {

        Game game = createGame();
        when(repository.save(game))
                .thenReturn(Game.builder()
                        .id(10)
                        .title("God Of War")
                        .genre("Ação")
                        .code("102030")
                        .build());

        Game savedGame = gameService.save(game);

        assertThat(savedGame.getId()).isNotNull();
        assertThat(savedGame.getTitle()).isEqualTo("God Of War");
        assertThat(savedGame.getGenre()).isEqualTo("Ação");
        assertThat(savedGame.getCode()).isEqualTo("102030");
    }

    @Test
    @DisplayName("Deve lançar erro de negócio ao tentar salvar um game com um code já utlizado")
    public void shouldNotSaveAGameWithDuplicatedCode() {

        Game game = createGame();

        when(repository.existsByCode(Mockito.anyString())).thenReturn(true);

        Throwable exception = Assertions.catchThrowable(() -> gameService.save(game));


        assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage("Code já cadastrado");

        Mockito.verify(repository, Mockito.never()).save(game);

    }


    private Game createGame() {
        return Game.builder().id(10000000).title("God Of War").genre("Ação").code("102030").build();
    }

}


