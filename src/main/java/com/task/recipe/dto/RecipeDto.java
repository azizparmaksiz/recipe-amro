package com.task.recipe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeDto {

    private String id;
    @NonNull
    private String name;
    @NonNull
    private boolean vegetarian;
    @NonNull
    private Integer servingNum;
    @NonNull
    private List<IngredientDto> ingredients;
    @NonNull
    private String instruction;
}
