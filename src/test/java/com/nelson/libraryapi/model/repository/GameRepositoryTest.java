package com.nelson.libraryapi.model.repository;

import com.nelson.libraryapi.api.dto.GameDTO;
import com.nelson.libraryapi.model.entity.Game;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class GameRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    GameRepository repository;

    @Test
    @DisplayName("Deve retornar true caso já exista um game na base com o code informado")
    public void returnTrueWhenCodeExists() {

        String code = "12345";

        Game game = createGame();
        entityManager.persist(game);

        boolean exists = repository.existsByCode(code);

        assertThat(exists).isTrue();

    }

    @Test
    @DisplayName("Deve retornar false caso não exista um game na base com o code informado")
    public void returnFalseWhenCodeDoesntExists() {

        String code = "12345";

        boolean exists = repository.existsByCode(code);

        assertThat(exists).isFalse();

    }

    private Game createGame() {
        return Game.builder().title("God Of War").genre("Ação").code("123").build();
    }


}
