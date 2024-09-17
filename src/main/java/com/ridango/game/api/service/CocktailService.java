package com.ridango.game.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ridango.game.*;
import com.ridango.game.api.dto.CocktailDTO;
import com.ridango.game.api.entity.CocktailEntity;
import com.ridango.game.api.repository.CocktailRepository;
import com.ridango.game.api.response.CocktailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CocktailService {
    private final CocktailRepository cocktailRepository;
    private final CocktailRestClient cocktailRestClient;
    private final ObjectMapper objectMapper;

    private WordRevealer wordRevealer;
    private HintProvider hintProvider;


    public CocktailEntity setupGame(){
        Drink drink = cocktailRestClient.getDrink();

        this.wordRevealer = new WordRevealer(drink.strDrink());
        this.hintProvider = new HintProvider(drink);

        String cocktailName = drink.strDrink();

        return CocktailEntity
                .builder()
                .attempts(5)
                .score(0)
                .strInstructions(drink.cutStrInstructions())
                .drinkName(cocktailName)
                .highestScore(0)
                .usedDrinkNames(Arrays.asList())
                .hiddenName(cocktailName.replaceAll("[a-zA-Z0-9]", "_"))
                .drinkHints(Arrays.asList(
                        drink.strCategory(),
                        drink.strGlass(),
                        drink.strDrinkThumb(),
                        drink.strAlcoholic()))
                .build();
    }


    public CocktailResponse createGame(){
        CocktailEntity newCocktailEntity = setupGame();

        cocktailRepository.save(newCocktailEntity);

        return CocktailResponse
                .builder()
                .score(newCocktailEntity.getScore())
                .attempts(newCocktailEntity.getAttempts())
                .hiddenName(newCocktailEntity.getHiddenName())
                .highestScore(newCocktailEntity.getHighestScore())
                .drinkHints(newCocktailEntity.getDrinkHints())
                .strInstructions(newCocktailEntity.getStrInstructions())
                .id(newCocktailEntity.getId())
                .build();
    }



    public CocktailEntity getCocktailById(Long id){
        Optional<CocktailEntity> cocktailEntity = cocktailRepository.findById(id);
        if (cocktailEntity.isEmpty()){
            throw new RuntimeException("");
        }
        return cocktailEntity.get();
    }

    public CocktailResponse getCocktailGameById(Long id){
        CocktailEntity cocktailEntity = getCocktailById(id);
        this.hintProvider = new HintProvider(cocktailEntity.getDrinkHints());

        return CocktailResponse.builder()
                .score(cocktailEntity.getScore())
                .highestScore(cocktailEntity.getHighestScore())
                .attempts(cocktailEntity.getAttempts())
                .strInstructions(cocktailEntity.getStrInstructions())
                .hiddenName(cocktailEntity.getHiddenName())
                .drinkHints(this.hintProvider.getHints(cocktailEntity.getAttempts()))
                .id(cocktailEntity.getId())
                .build();
    }

    public boolean IsCocktailAppeared(Long id, String drinkName){
        CocktailEntity cocktailEntity = getCocktailById(id);

        return cocktailEntity.getUsedDrinkNames().contains(drinkName);

    }

    public List<CocktailResponse> getAllCocktails(){
        List<CocktailEntity> allCocktails = cocktailRepository.findAll();
        return allCocktails.stream().map(entity ->
                objectMapper.convertValue(entity, CocktailResponse.class)
        ).toList();

    }

    public CocktailResponse resetGame(CocktailEntity cocktailEntity){
        // If the user didn't guess the drink name at all, just update an old record instead of creating new one
        CocktailEntity newCocktailEntity = setupGame();

        List<String> usedDrinkNames = cocktailEntity.getUsedDrinkNames();

        usedDrinkNames.add(cocktailEntity.getDrinkName());

        // Updating an id
        newCocktailEntity.setId(cocktailEntity.getId());

        newCocktailEntity.setHighestScore(cocktailEntity.getHighestScore());
        newCocktailEntity.setUsedDrinkNames(cocktailEntity.getUsedDrinkNames());

        cocktailRepository.save(newCocktailEntity);

        return CocktailResponse
                .builder()
                .score(newCocktailEntity.getScore())
                .attempts(newCocktailEntity.getAttempts())
                .highestScore(newCocktailEntity.getHighestScore())
                .hiddenName(newCocktailEntity.getHiddenName())
                .strInstructions(newCocktailEntity.getStrInstructions())
                .drinkHints(this.hintProvider.getHints(cocktailEntity.getAttempts()))
                .id(newCocktailEntity.getId())
                .build();
    }


    public CocktailResponse playGame(Long id, CocktailDTO cocktailDTO){

        CocktailEntity cocktailEntity = getCocktailById(id);


        if (IsCocktailAppeared(id, cocktailEntity.getDrinkName())){

            Drink drink = cocktailRestClient.getDrink();

            cocktailEntity.setDrinkName(drink.strDrink());

            cocktailEntity.setHiddenName(drink.strDrink().replaceAll("[a-zA-Z0-9]", "_"));
            cocktailEntity.setStrInstructions(drink.cutStrInstructions());
            cocktailEntity.setDrinkHints(new ArrayList<>(Arrays.asList(
                    drink.strCategory(),
                    drink.strGlass(),
                    drink.strDrinkThumb(),
                    drink.strAlcoholic())));

            cocktailRepository.save(cocktailEntity);

        }


        this.wordRevealer = new WordRevealer(cocktailEntity.getDrinkName());

        this.hintProvider = new HintProvider(cocktailEntity.getDrinkHints());


        if (cocktailEntity.getAttempts() != 0){

            StringBuilder hiddenWord = new StringBuilder(cocktailEntity.getHiddenName());

            // If the user is guessed cocktail drink correctly
            if (WordUtility.checkUserWordAgainstOriginalWords(cocktailDTO.getUserWord(), cocktailEntity.getDrinkName())){
                // Getting all used drink names
                List<String> usedDrinkNames = cocktailEntity.getUsedDrinkNames();

                // Adding current drink name
                usedDrinkNames.add(cocktailEntity.getDrinkName());

                // Creating a new game
                CocktailEntity newCocktailEntity = setupGame();

                // Instead of creating a new record just updating the id
                newCocktailEntity.setId(cocktailEntity.getId());

                // Updating the used drink names
                newCocktailEntity.setUsedDrinkNames(usedDrinkNames);

                // Updating the score
                newCocktailEntity.setScore(cocktailEntity.getScore() + cocktailEntity.getAttempts());

                if(newCocktailEntity.getScore() > cocktailEntity.getHighestScore()){
                    newCocktailEntity.setHighestScore(newCocktailEntity.getScore());

                }else{
                    newCocktailEntity.setHighestScore(cocktailEntity.getHighestScore());
                }

                // Saving to database
                cocktailRepository.save(newCocktailEntity);

                return CocktailResponse
                        .builder()
                        .score(newCocktailEntity.getScore())
                        .highestScore(newCocktailEntity.getHighestScore())
                        .attempts(newCocktailEntity.getAttempts())
                        .strInstructions(newCocktailEntity.getStrInstructions())
                        .hiddenName(newCocktailEntity.getHiddenName())
                        .drinkHints(this.hintProvider.getHints(cocktailEntity.getAttempts()))
                        .id(newCocktailEntity.getId())
                        .build();


            }else{

                if (cocktailEntity.getAttempts() - 1 != 0){

                    this.wordRevealer.revealRandomLetter(hiddenWord);

                    cocktailEntity.setAttempts(cocktailEntity.getAttempts() - 1);

                    cocktailEntity.setHiddenName(hiddenWord.toString());

                    cocktailRepository.save(cocktailEntity);

                    return CocktailResponse
                            .builder()
                            .score(cocktailEntity.getScore())
                            .attempts(cocktailEntity.getAttempts())
                            .highestScore(cocktailEntity.getHighestScore())
                            .hiddenName(cocktailEntity.getHiddenName())
                            .strInstructions(cocktailEntity.getStrInstructions())
                            .drinkHints(this.hintProvider.getHints(cocktailEntity.getAttempts()))
                            .id(cocktailEntity.getId())
                            .build();
                }else{

                    return resetGame(cocktailEntity);

                }
            }

        }else{

            return resetGame(cocktailEntity);

        }

    }
}
