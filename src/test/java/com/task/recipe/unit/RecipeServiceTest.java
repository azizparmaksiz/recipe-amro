package com.task.recipe.unit;

import com.task.recipe.dto.IngredientDto;
import com.task.recipe.dto.RecipeDto;
import com.task.recipe.exception.RecipeNotFoundException;
import com.task.recipe.model.Recipe;
import com.task.recipe.repository.RecipeRepository;
import com.task.recipe.service.RecipeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class RecipeServiceTest {

    @Autowired
    private RecipeService service;
    @Autowired
    private RecipeRepository repository;


    @AfterEach
    public void tearDown() {
        repository.deleteAll();
    }

    @BeforeEach
    public void setup() {
        repository.deleteAll();
    }

    @Test()
    public void testAddRecipe() {

        // given
        RecipeDto recipeDto = createDummyRecipe();

        // when
        String id = service.save(recipeDto);

        // then
        Recipe recipe = repository.findById(id).orElse(null);

        assertNotNull(recipe);
        assertEquals(recipe.getId(), id);
        assertEquals(recipe.getServingNum(), recipeDto.getServingNum());
        assertTrue(recipe.isVegetarian());
        assertEquals(recipe.getName(), recipeDto.getName());
        assertEquals(recipe.getIngredients().size(), 4);

    }

    @DisplayName(" Get recipe detail with a wrong id will throw RecipeNotFoundException")
    @Test()
    public void testRecipeDetailByIdGivesError() {

        // given
        String id = "dummy-id";
        try {
            // when
            service.detail(id);
        } catch (Exception e) {

            // then
            assertTrue(e instanceof RecipeNotFoundException);
            assertEquals(e.getMessage(), "Recipe " + id + " Not Found");
        }

    }

    @Test
    public void testRecipeDetail() {
        // given
        RecipeDto recipeDto = createDummyRecipe();

        String id = service.save(recipeDto);

        // when
        RecipeDto detail = service.detail(id);

        // then
        assertEquals(detail.getId(), id);
        assertEquals(detail.getServingNum(), recipeDto.getServingNum());
        assertTrue(detail.isVegetarian());
        assertEquals(detail.getName(), recipeDto.getName());
        assertEquals(detail.getIngredients().size(), 4);

    }


    @DisplayName("Delete recipe with a wrong id will throw RecipeNotFoundException")
    @Test()
    public void deleteRecipeByIdGivesError() {
        // given
        String id = "dummy-id";
        try {
            // when
            service.delete(id);
        } catch (Exception e) {

            // then
            assertTrue(e instanceof RecipeNotFoundException);
            assertEquals(e.getMessage(), "Recipe " + id + " Not Found");
        }

    }

    @Test
    public void deleteRecipe() {

        // given
        RecipeDto recipeDto = createDummyRecipe();

        String id = service.save(recipeDto);

        boolean isContain = repository.findById(id).isPresent();

        RecipeDto detail = service.detail(id);

        assertNotNull(detail);
        assertTrue(isContain);

        // when
        service.delete(id);

        // then
        isContain = repository.findById(id).isPresent();
        assertFalse(isContain);
    }

    @Test
    public void testFilterVegetarianRecipeFilter() {
        // given
        addThreeRecipe();

        /// TEST VEGETARIAN FILTER

        boolean vegetarian = true;
        // when
        List<RecipeDto> recipeDtos = service.filter(vegetarian, null, null, null, null);

        // then
        assertEquals(2, recipeDtos.size());
        assertTrue(recipeDtos.stream().allMatch(RecipeDto::isVegetarian));

        // when
        recipeDtos = service.filter(false, null, null, null, null);

        // then
        assertEquals(1, recipeDtos.size());

    }

    @Test
    public void testServingNumFilter() {
        // given
        addThreeRecipe();

        // when
        int servingNum = 2;
        List<RecipeDto> recipeDtos = service.filter(null, servingNum, null, null, null);

        // then
        assertEquals(2, recipeDtos.size());
        assertTrue(recipeDtos.stream().allMatch(item -> item.getServingNum() == 2));


        // TEST INGREDIENT INCLUDE FILTER

        // when
        String[] includeIngredient = new String[]{"banana"};
        recipeDtos = service.filter(null, null, includeIngredient, null, null);

        // then
        assertEquals(3, recipeDtos.size());
        assertTrue(recipeDtos.stream().allMatch(item -> item.getIngredients().stream().anyMatch(x -> x.getName().equalsIgnoreCase(includeIngredient[0]))));


        // TEST INGREDIENT EXCLUDE FILTER

        // when
        String[] excludeIngredient = new String[]{"meat"};
        recipeDtos = service.filter(null, null, null, excludeIngredient, null);

        // then
        assertEquals(2, recipeDtos.size());
        assertTrue(recipeDtos.stream().allMatch(item -> item.getIngredients().stream().noneMatch(x -> x.getName().equalsIgnoreCase(excludeIngredient[0]))));


        // TEST INSTRUCTION  FILTER

        // when
        String instruction = "smoke";
        recipeDtos = service.filter(null, null, null, null, instruction);

        // then
        assertEquals(1, recipeDtos.size());
        assertTrue(recipeDtos.stream().allMatch(item -> item.getInstruction().toLowerCase().contains(instruction)));

    }

    @Test
    public void testIngredientFilter() {
        // given
        addThreeRecipe();

        // when
        String[] includeIngredient = new String[]{"Banana"};
        List<RecipeDto> recipeDtos = service.filter(null, null, includeIngredient, null, null);

        // then
        assertEquals(3, recipeDtos.size());
        assertTrue(recipeDtos.stream().allMatch(item -> item.getIngredients().stream().anyMatch(x -> x.getName().equalsIgnoreCase(includeIngredient[0]))));


        // TEST INGREDIENT EXCLUDE FILTER

        // when
        String[] excludeIngredient = new String[]{"meat"};
        recipeDtos = service.filter(null, null, null, excludeIngredient, null);

        // then
        assertEquals(2, recipeDtos.size());
        assertTrue(recipeDtos.stream().allMatch(item -> item.getIngredients().stream().noneMatch(x -> x.getName().equalsIgnoreCase(excludeIngredient[0]))));


        // TEST INSTRUCTION  FILTER

        // when
        String instruction = "smoke";
        recipeDtos = service.filter(null, null, null, null, instruction);

        // then
        assertEquals(1, recipeDtos.size());
        assertTrue(recipeDtos.stream().allMatch(item -> item.getInstruction().toLowerCase().contains(instruction)));

    }
    @Test
    public void testInstructionFilter() {
        // given
        addThreeRecipe();

        // when
        String instruction = "smoke";
        List<RecipeDto> recipeDtos = service.filter(null, null, null, null, instruction);

        // then
        assertEquals(1, recipeDtos.size());
        assertTrue(recipeDtos.stream().allMatch(item -> item.getInstruction().toLowerCase().contains(instruction)));

    }

    private void addThreeRecipe() {
        // given
        RecipeDto aRecipe = createDummyRecipe();
        aRecipe.getIngredients().add(new IngredientDto("meat", 0.1, "x"));
        aRecipe.setVegetarian(false);
        aRecipe.setServingNum(2);
        aRecipe.setInstruction("Oven");

        RecipeDto aRecipe2 = createDummyRecipe();
        aRecipe2.getIngredients().add(new IngredientDto("milk", 0.1, "Y"));
        aRecipe2.setServingNum(2);
        aRecipe2.setInstruction("Oven then grill");

        RecipeDto aRecipe3 = createDummyRecipe();
        aRecipe3.getIngredients().add(new IngredientDto("nut", 0.1, "Z"));
        aRecipe3.setServingNum(3);
        aRecipe3.setInstruction("Smoke");


        service.save(aRecipe);
        service.save(aRecipe2);
        service.save(aRecipe3);
    }


    private RecipeDto createDummyRecipe() {
        return new RecipeDto(null,
                "browni",
                true,
                3,
                new ArrayList(List.of(
                        new IngredientDto("cacao", 50.0, "g"),
                        new IngredientDto("flour", 200.0, "g"),
                        new IngredientDto("coco milk", 0.1, "lt"),
                        new IngredientDto("banana", 300.0, "g")
                )),
                "smash banana, then add cacao and coco milk then and flour");
    }

}
