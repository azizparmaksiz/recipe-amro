package com.task.recipe.service;

import com.task.recipe.dto.RecipeDto;
import com.task.recipe.exception.RecipeNotFoundException;
import com.task.recipe.model.Recipe;
import com.task.recipe.repository.RecipeRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j

public class RecipeServiceImpl implements RecipeService {

    private MongoTemplate mongoTemplate;
    private RecipeRepository repository;
    private ModelMapper modelMapper;

    @Override
    public String save(RecipeDto recipeDto) {
        log.info("Recipe {} save action called", recipeDto);
        Recipe recipe = modelMapper.map(recipeDto, Recipe.class);
        recipe.getIngredients().forEach(item->item.setName(item.getName().toLowerCase()));
        repository.save(recipe);
        log.info("Recipe saved");
        return recipe.getId();
    }

    @Override
    public RecipeDto detail(String recipeId) {
        Optional<Recipe> recipeOptional = repository.findById(recipeId);
        if (recipeOptional.isEmpty()) {
            log.error("Recipe id {} not found", recipeId);
            throw new RecipeNotFoundException("Recipe "+recipeId+" Not Found");
        }
        return modelMapper.map(recipeOptional.get(), RecipeDto.class);
    }

    @Override
    public void delete(String id) {
        Optional<Recipe> recipe = repository.findById(id);
        recipe.ifPresentOrElse(value -> {
                    repository.delete(value);
                    log.info("Recipe {} deleted", recipe);
                }
                , () -> {
                    log.error("Recipe id {} not found", id);
                    throw new RecipeNotFoundException("Recipe "+id+" Not Found");
                });
    }

    @Override
    public List<RecipeDto> filter(Boolean vegetarian, Integer servingNum, String[] ingredientsInc, String[] ingredientsExc, String instructionQuery) {

        Query query = new Query();

        Criteria criteria = new Criteria();

        List<Criteria> criteriaList = new ArrayList<>();

        if (vegetarian != null) {
            criteriaList.add(Criteria.where("vegetarian").is(vegetarian));
        }
        if (servingNum != null) {
            criteriaList.add(Criteria.where("servingNum").is(servingNum));
        }
        if (ingredientsInc != null && ingredientsInc.length > 0) {
            criteriaList.add(Criteria.where("ingredients.name").in(Arrays.stream(ingredientsInc).map(String::toLowerCase).collect(Collectors.toList())));
        }
        if (ingredientsExc != null && ingredientsExc.length > 0) {
            criteriaList.add(Criteria.where("ingredients.name").nin(Arrays.stream(ingredientsExc).map(String::toLowerCase).collect(Collectors.toList())));
        }
        if (!Strings.isEmpty(instructionQuery)) {
            criteriaList.add(Criteria.where("instruction").regex(instructionQuery,"i"));
        }
        if (criteriaList.isEmpty()) {
            query.addCriteria(criteria);
        } else {
            query.addCriteria(criteria.andOperator(criteriaList));
        }
        List<Recipe> recipes = mongoTemplate.find(query, Recipe.class);
        Type listType = new TypeToken<List<RecipeDto>>() {
        }.getType();
        return modelMapper.map(recipes, listType);
    }


}
