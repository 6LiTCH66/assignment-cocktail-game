package com.ridango.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class WordUtility {
    public static boolean checkUserWordAgainstOriginalWords(String userWord, String originalWord) {
        return userWord.equalsIgnoreCase(originalWord);
    }

    public static void removeRevealedLetterFromHashMap(HashMap<Character, ArrayList<Integer>> strDrinkHashMap, Character letter) {
        strDrinkHashMap.remove(letter);

    }

    public static HashMap<Character, ArrayList<Integer>> getRandomLetterPosition(HashMap<Character, ArrayList<Integer>> strDrinkHashMap) {
        List<Character> keys = new ArrayList<>(strDrinkHashMap.keySet());

        if (keys.isEmpty()) {
            return new HashMap<>();
        }

        HashMap<Character, ArrayList<Integer>> randomLetterAndPosition = new HashMap<>();
        Random random = new Random();

        int randomIndex = random.nextInt(keys.size());
        Character randomKey = keys.get(randomIndex);

        randomLetterAndPosition.put(randomKey, strDrinkHashMap.get(randomKey));

        return randomLetterAndPosition;
    }

    public static HashMap<Character, ArrayList<Integer>> stringToHashMap(String str) {

        HashMap<Character, ArrayList<Integer>> map = new HashMap<>();

        for (int i = 0; i < str.length(); i++) {

            char c = str.toLowerCase().charAt(i);

            if (c != ' '){
                if (map.containsKey(c)) {

                    map.get(c).add(i);

                }
                else{

                    ArrayList<Integer> indexList = new ArrayList<>();
                    indexList.add(i);
                    map.put(c, indexList);

                }
            }

        }

        return map;
    }
}
