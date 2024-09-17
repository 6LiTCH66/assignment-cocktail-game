package com.ridango.game.api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CocktailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int attempts;

    private int score;

    private String drinkName;

    private String hiddenName;

    private String strInstructions;

    private int highestScore;

    @ElementCollection
    private List<String> usedDrinkNames;

    @ElementCollection
    private List<String> drinkHints;

}
