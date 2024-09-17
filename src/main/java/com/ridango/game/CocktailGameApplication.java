package com.ridango.game;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class CocktailGameApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(CocktailGameApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

	}

//	@Bean
//	CommandLineRunner runner(CocktailGame cocktailGame, CocktailRestClient restClient) {
//		return args -> {
//			while (true) {
//				cocktailGame.startGame();
//
//				if (cocktailGame.isGameEnded()){
//					break;
//				}
//
//				cocktailGame.updateGame();
//			}
//
//		};
//	}
}
