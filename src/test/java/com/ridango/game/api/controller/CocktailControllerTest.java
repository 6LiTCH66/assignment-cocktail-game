package com.ridango.game.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ridango.game.api.dto.CocktailDTO;
import com.ridango.game.api.response.CocktailResponse;
import com.ridango.game.api.service.CocktailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(CocktailController.class)
class CocktailControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CocktailService cocktailService;

    @Autowired
    private ObjectMapper objectMapper;

    private CocktailResponse mockCocktailResponse;


    @BeforeEach
    void setUp() {
        mockCocktailResponse = CocktailResponse.builder()
                .id(1L)
                .score(10)
                .attempts(5)
                .highestScore(20)
                .hiddenName("M______a")
                .drinkHints(Arrays.asList("Category", "Glass", "Image URL", "Alcoholic"))
                .strInstructions("Shake with ice")
                .build();
    }

    @Test
    void testGetHello() throws Exception {
        mockMvc.perform(get("/api/cocktail"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello"));
    }

    @Test
    void testCreateGame() throws Exception {
        Mockito.when(cocktailService.createGame()).thenReturn(mockCocktailResponse);

        mockMvc.perform(post("/api/cocktail/create"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.score").value(10))
                .andExpect(jsonPath("$.attempts").value(5))
                .andExpect(jsonPath("$.highestScore").value(20))
                .andExpect(jsonPath("$.hiddenName").value("M______a"))
                .andExpect(jsonPath("$.drinkHints[0]").value("Category"))
                .andExpect(jsonPath("$.strInstructions").value("Shake with ice"));
    }

    @Test
    void testGetAllCocktails() throws Exception {
        // Mock the service call
        List<CocktailResponse> mockResponses = Arrays.asList(mockCocktailResponse);
        Mockito.when(cocktailService.getAllCocktails()).thenReturn(mockResponses);

        mockMvc.perform(get("/api/cocktail/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].score").value(10))
                .andExpect(jsonPath("$[0].attempts").value(5))
                .andExpect(jsonPath("$[0].highestScore").value(20))
                .andExpect(jsonPath("$[0].hiddenName").value("M______a"))
                .andExpect(jsonPath("$[0].drinkHints[0]").value("Category"))
                .andExpect(jsonPath("$[0].strInstructions").value("Shake with ice"));
    }

    @Test
    void testGetCocktailById() throws Exception {
        // Mock the service call
        Mockito.when(cocktailService.getCocktailGameById(anyLong())).thenReturn(mockCocktailResponse);

        mockMvc.perform(get("/api/cocktail/game/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.score").value(10))
                .andExpect(jsonPath("$.attempts").value(5))
                .andExpect(jsonPath("$.highestScore").value(20))
                .andExpect(jsonPath("$.hiddenName").value("M______a"))
                .andExpect(jsonPath("$.drinkHints[0]").value("Category"))
                .andExpect(jsonPath("$.strInstructions").value("Shake with ice"));
    }

    @Test
    void testPlayGame() throws Exception {
        // Mock the service call
        Mockito.when(cocktailService.playGame(anyLong(), any(CocktailDTO.class))).thenReturn(mockCocktailResponse);

        CocktailDTO mockDTO = new CocktailDTO();
        mockDTO.setUserWord("Margarita");

        mockMvc.perform(post("/api/cocktail/play/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.score").value(10))
                .andExpect(jsonPath("$.attempts").value(5))
                .andExpect(jsonPath("$.highestScore").value(20))
                .andExpect(jsonPath("$.hiddenName").value("M______a"))
                .andExpect(jsonPath("$.drinkHints[0]").value("Category"))
                .andExpect(jsonPath("$.strInstructions").value("Shake with ice"));
    }


}