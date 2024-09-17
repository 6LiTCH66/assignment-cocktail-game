package com.ridango.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HintProvider {
    private final List<String> cocktailHints;
    private int hintCounter;

    public HintProvider(Drink drink) {
        this.cocktailHints = Arrays.asList(
                drink.strCategory(),
                drink.strGlass(),
                drink.strDrinkThumb(),
                drink.strAlcoholic()

        );
        this.hintCounter = 0;
    }

    public HintProvider(List<String> hints) {
        this.cocktailHints = new ArrayList<>(hints);

        this.hintCounter = 0;
    }


    public List<String> getHints(){
        this.hintCounter++;

        if (hintCounter > cocktailHints.size()){
            this.hintCounter = cocktailHints.size();
        }

        return this.cocktailHints.subList(0, this.hintCounter);

    }


    public List<String> getHints(int attempts){

        this.hintCounter = Math.max(1, 4 - attempts);

        return this.cocktailHints.subList(0, hintCounter);

    }




}
