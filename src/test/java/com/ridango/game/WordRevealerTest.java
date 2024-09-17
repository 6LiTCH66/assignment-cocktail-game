package com.ridango.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class WordRevealerTest {

    private WordRevealer wordRevealer;
    private String cocktailName;

    @BeforeEach
    void setUp() {
        cocktailName = "Margarita";
        wordRevealer = new WordRevealer(cocktailName);
    }


    @Test
    void testRevealRandomLetter_singleLetterRevealed() {
        StringBuilder hiddenWord = new StringBuilder("_________");

        try (MockedStatic<WordUtility> mockedUtility = Mockito.mockStatic(WordUtility.class)) {

            HashMap<Character, ArrayList<Integer>> mockedMap = new HashMap<>();
            ArrayList<Integer> positions = new ArrayList<>();
            positions.add(0);
            mockedMap.put('M', positions);

            mockedUtility.when(() -> WordUtility.stringToHashMap(cocktailName))
                    .thenReturn(mockedMap);

            mockedUtility.when(() -> WordUtility.getRandomLetterPosition(Mockito.any()))
                    .thenReturn(mockedMap);

            wordRevealer.revealRandomLetter(hiddenWord);

            assertEquals('M', hiddenWord.charAt(0));
            assertEquals("M________", hiddenWord.toString());
        }
    }

    @Test
    void testRevealRandomLetter_twoLettersRevealed() {
        StringBuilder hiddenWord = new StringBuilder("_________");

        try (MockedStatic<WordUtility> mockedUtility = Mockito.mockStatic(WordUtility.class)) {
            HashMap<Character, ArrayList<Integer>> mockedMap = new HashMap<>();
            ArrayList<Integer> positionsM = new ArrayList<>();
            positionsM.add(0);
            mockedMap.put('M', positionsM);

            ArrayList<Integer> positionsA = new ArrayList<>();
            positionsA.add(1);
            mockedMap.put('a', positionsA);

            mockedUtility.when(() -> WordUtility.stringToHashMap(cocktailName))
                    .thenReturn(mockedMap);

            mockedUtility.when(() -> WordUtility.getRandomLetterPosition(Mockito.any()))
                    .thenReturn(mockedMap);

            wordRevealer.revealRandomLetter(hiddenWord);

            assertEquals('M', hiddenWord.charAt(0));
            assertEquals('a', hiddenWord.charAt(1));
            assertEquals("Ma_______", hiddenWord.toString());
        }
    }

}