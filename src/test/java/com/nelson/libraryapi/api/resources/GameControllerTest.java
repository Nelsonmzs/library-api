package com.nelson.libraryapi.api.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nelson.libraryapi.api.dto.GameDTO;
import com.nelson.libraryapi.exception.BusinessException;
import com.nelson.libraryapi.model.entity.Game;
import com.nelson.libraryapi.service.GameService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest
@AutoConfigureMockMvc
public class GameControllerTest {

    static String GAME_API = "/api/games";

    @Autowired
    MockMvc mvc;

    @MockBean
    GameService service;

    @Test
    @DisplayName("Deve criar um game com sucesso.")
    public void createGameTest() throws Exception {

        GameDTO dto = createGame();

        Game savedGame = Game.builder().id(10).title("Pro Evolution Soccer").genre("Esporte").code("12345").build();

        BDDMockito.given(service.save(Mockito.any(Game.class))).willReturn(savedGame);

        String json = new ObjectMapper().writeValueAsString(dto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(GAME_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc
                .perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("id").value(10))
                .andExpect(jsonPath("title").value(dto.getTitle()))
                .andExpect(jsonPath("genre").value(dto.getGenre()))
                .andExpect(jsonPath("code").value(dto.getCode()));

    }

    private GameDTO createGame() {
        return GameDTO.builder().title("Pro Evolution Soccer").genre("Esporte").code("12345").build();
    }

    @Test
    @DisplayName("Deve lançar erro de validação quando não houver dados suficientes para a criação de um game.")
    public void createInvalidGameTest() throws Exception {

        String json = new ObjectMapper().writeValueAsString(new GameDTO());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(GAME_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc
                .perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", hasSize(3)));

    }

    @Test
    @DisplayName("Deve lançar erro ao tentar cadastrar um game com o code já utilizado por outro.")
    public void createGameWithDuplicateCode() throws Exception {

        GameDTO dto = createGame();

        String json = new ObjectMapper().writeValueAsString(dto);

        String mensagem = "Code já cadastrado";

        BDDMockito.given(service.save(Mockito.any(Game.class)))
                .willThrow(new BusinessException(mensagem));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(GAME_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc
                .perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", hasSize(1)))
                .andExpect(jsonPath("errors[0]").value(mensagem));

    }

}
