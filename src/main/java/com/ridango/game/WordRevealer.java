package com.ridango.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WordRevealer {
    private final char[] strDrinkArray;
    private final HashMap<Character, ArrayList<Integer>> strDrinkHashMap;

    public WordRevealer(String cocktailName) {
        this.strDrinkArray = cocktailName.toCharArray();
        this.strDrinkHashMap = WordUtility.stringToHashMap(cocktailName);
    }

    private void updateStrDrinkHashMap(StringBuilder hiddenName){

        for (int i = 0; i < hiddenName.length(); i++) {

            char letter = hiddenName.toString().toLowerCase().charAt(i);

            if (letter != '_') {
                this.strDrinkHashMap.remove(letter);
            }
        }
    }


    public void revealRandomLetter(StringBuilder hiddenWord) {

        updateStrDrinkHashMap(hiddenWord);

        int lettersToReveal = 1;

        if (this.strDrinkHashMap.size() > 10) {
            lettersToReveal = 3;
        }
        else if(this.strDrinkHashMap.size() > 5){
            lettersToReveal = 2;
        }


        for (int i = 0; i < lettersToReveal; i++) {

            HashMap<Character, ArrayList<Integer>> randomLetterAndPositions = WordUtility.getRandomLetterPosition(strDrinkHashMap);

            for (Map.Entry<Character, ArrayList<Integer>> entry : randomLetterAndPositions.entrySet()) {
                for (Integer position : entry.getValue()) {

                    hiddenWord.setCharAt(position, strDrinkArray[position]);
                }

                WordUtility.removeRevealedLetterFromHashMap(strDrinkHashMap, entry.getKey());
            }
        }

    }

}
