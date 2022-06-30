package com.task.recipe.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@Document
public class Recipe {

    @Id
    private String id;
    private String name;
    private Integer servingNum;
    private String instruction;
    private boolean vegetarian;
    private List<Ingredient> ingredients;
}
