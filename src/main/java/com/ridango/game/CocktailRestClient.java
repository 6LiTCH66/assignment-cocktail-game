package com.ridango.game;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Component
public class CocktailRestClient {
    private final RestClient restClient;


    public CocktailRestClient(RestClient.Builder builder) {
        this.restClient = builder.baseUrl("https://www.thecocktaildb.com/api/json/v1/1/").build();
    }

    public Drink getDrink() {
        Map<String, List<Drink>> response = restClient.get()
                .uri("random.php")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        return response.get("drinks").get(0);
    }
}
