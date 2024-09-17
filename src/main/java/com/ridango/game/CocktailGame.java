package com.ridango.game;

import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

@Service
public class CocktailGame {
    private Drink drink;
    private StringBuilder hiddenLetters;
    private WordRevealer wordRevealer;
    private HintProvider hintProvider;
    @Getter
    private int attempts;
    private int score;
    private final CocktailRestClient restClient;

    public CocktailGame(CocktailRestClient restClient) {
        this.restClient = restClient;
        this.score = 0;
        setupGame();

    }

    public void updateGame() {
        setupGame();

    }

    public boolean isGameEnded() {
        return attempts == 0;
    }

    private void setupGame(){
        this.drink = this.restClient.getDrink();
        this.hiddenLetters = new StringBuilder(this.drink.strDrink().replaceAll("[a-zA-Z0-9]", "_"));

        this.wordRevealer = new WordRevealer(drink.strDrink());

        this.hintProvider = new HintProvider(this.drink);
        this.attempts = 5;

        if (this.drink.strDrink().length() < this.attempts) {
            this.attempts = this.drink.strDrink().length() -1;
        }
    }


    public void startGame() {
        Scanner scanner = new Scanner(System.in);


        while (attempts != 0) {
            System.out.println(this.drink.strDrink());
            System.out.printf("You have %s attempts to guess the cocktail name!%n", attempts);
            System.out.println("Here is the instructions to make this cocktail: " + this.drink.strInstructions());

            System.out.println("Here is the number of letters: " + hiddenLetters);

            System.out.print("Enter name of cocktail: ");
            String userWords = scanner.nextLine();

            if (WordUtility.checkUserWordAgainstOriginalWords(userWords, this.drink.strDrink())) {
                System.out.println("Congratulations! You've guessed the cocktail.");
                this.score += this.attempts;
                System.out.println("You score is: " + this.score);
                break;

            } else {
                System.out.println();
                System.out.println("Here are some hints to help you guess the name of the cocktail:");

                List<String> hints = hintProvider.getHints();

                IntStream.range(0, hints.size())
                        .forEach(i -> System.out.println((i + 1) + ": " + hints.get(i)));

                System.out.println();
                wordRevealer.revealRandomLetter(hiddenLetters);
            }

            attempts--;

            if (attempts == 0) {
                System.out.println("Sorry, you've run out of attempts.");
                System.out.println("The hidden words was: " + drink.strDrink());
                System.out.println("The game ended!");
            }
        }
    }
}
