package com.ridango.game.api.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CocktailResponse {

    private Long id;

    private int attempts;

    private int score;

    private String hiddenName;
    private String strInstructions;

    private int highestScore;

    private List<String> drinkHints;
}
