package com.task.recipe.service;

import com.task.recipe.dto.RecipeDto;

import java.util.List;

public interface RecipeService {
    String save(RecipeDto recipeDto);

    void delete(String recipeName);

    List<RecipeDto> filter(Boolean vegetarian, Integer servingNum, String[] ingredientsInc, String[] ingredientsExc, String instructionQuery);

    RecipeDto detail(String recipeId);
}
