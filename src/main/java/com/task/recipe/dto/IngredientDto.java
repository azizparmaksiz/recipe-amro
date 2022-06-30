package com.task.recipe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IngredientDto {
    @NonNull
    private String name;
    @NonNull
    private Double amount;
    @NonNull
    private String unit;
}
