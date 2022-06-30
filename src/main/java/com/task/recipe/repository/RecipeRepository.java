package com.task.recipe.repository;

import com.task.recipe.model.Recipe;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecipeRepository extends MongoRepository<Recipe,String> {
    Optional<Recipe> findByName(String recipeName);
}
