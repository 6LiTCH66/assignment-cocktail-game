package com.ridango.game.api.controller;

import com.ridango.game.api.dto.CocktailDTO;
import com.ridango.game.api.response.CocktailResponse;
import com.ridango.game.api.service.CocktailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import java.util.List;



@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cocktail")
public class CocktailController {
    private final CocktailService cocktailService;

    @Operation(summary = "Just for a test", description = "This endpoint just tests if the server is running correctly")
    @GetMapping
    public String getHello(){
        return "Hello";
    }

    @Operation(summary = "Create a new game", description = "This endpoint creates a new game.")
    @PostMapping("/create")
    public CocktailResponse createGame(){
        return this.cocktailService.createGame();
    }

    @Operation(summary = "Retrieve all cocktails", description = "This endpoint retrieves all cocktails.")
    @GetMapping("/all")
    public List<CocktailResponse> getAllCocktails(){
        return this.cocktailService.getAllCocktails();
    }

    @Operation(summary = "Get cocktail by id", description = "Retrieve a specific cocktail.")
    @GetMapping("/game/{id}")
    public CocktailResponse getCocktailById(@PathVariable Long id){
        return this.cocktailService.getCocktailGameById(id);
    }

    @Operation(summary = "Play specific game by id", description = "This endpoint allows you to play specific game.")
    @PostMapping("/play/{id}")
    CocktailResponse playGame(@PathVariable Long id, @RequestBody CocktailDTO cocktailDTO){
        return this.cocktailService.playGame(id, cocktailDTO);
    }


}
