package com.ridango.game.api.repository;

import com.ridango.game.api.entity.CocktailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CocktailRepository extends JpaRepository<CocktailEntity, Long> {

}
