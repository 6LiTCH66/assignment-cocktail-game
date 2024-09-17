package com.ridango.game;

public record Drink(
        String strDrink,
        String strCategory,
        String strAlcoholic,
        String strGlass,
        String strInstructions,
        String strDrinkThumb
) {
    public String cutStrInstructions() {
        if (this.strInstructions.length() >= 254){
            return this.strInstructions.substring(0, 254);
        }
        return this.strInstructions;
    }
}
