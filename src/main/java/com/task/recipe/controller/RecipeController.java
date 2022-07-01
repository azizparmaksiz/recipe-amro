package com.task.recipe.controller;

import com.task.recipe.dto.RecipeDto;
import com.task.recipe.service.RecipeService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recipe")
@AllArgsConstructor
public class RecipeController {

    private RecipeService recipeService;

    @ApiOperation(value = "", notes = "This rest service provide you to save and update recipe." +
            " <br/> When providing valid recipe id this rest service will update the recipe." +
            " <br/>  With empty or not valid id this rest service will create new recipe ",
            nickname = "SaveRecipe")
    @ApiResponses(value = { @ApiResponse(code = 201, message = "Success|Created")})
    @PostMapping()
    public ResponseEntity<String> save(@RequestBody RecipeDto recipeDto) {

        HttpHeaders headerMap = new HttpHeaders();

        String recipeId = recipeService.save(recipeDto);
        headerMap.add("location", "/recipe/" + recipeId);

        return new ResponseEntity<>(recipeId, headerMap, HttpStatus.CREATED);
    }

    @ApiOperation(value = "", notes = "This rest service provide you to filter or search recipe." +
            " <br/> <strong>vegi</strong>:  if it is vegetarian" +
            " <br/> <strong>serveNum</strong>: serving for how many people" +
            " <br/> <strong>incIngr</strong>: Included Ingredients'" +
            " <br/> <strong>excIngr</strong>: Excluded Ingredients'" +
            " <br/> <strong>instr</strong>:  Search with Instruction '" +
            " <br/>  With empty param rest service will return the all recipes ",
            nickname = "FilterRecipe")
    @ApiResponses(value = { @ApiResponse(code = 201, message = "Success|Ok")})
    @GetMapping()
    public ResponseEntity<List<RecipeDto>> filter(
            @RequestParam(required = false, name = "vegi") Boolean vegetarian,
            @RequestParam(required = false, name = "serveNum") Integer serveNum,
            @RequestParam(required = false, name = "incIngr") String[] incIngr,
            @RequestParam(required = false, name = "excIngr") String[] excIngr,
            @RequestParam(required = false, name = "instr") String instr
    ) {

        return new ResponseEntity<>(recipeService.filter(vegetarian, serveNum, incIngr, excIngr, instr), HttpStatus.OK);
    }

    @ApiOperation(value = "", notes = "<body>This rest service provide you to get detail of the recipe with id.</body>",
            nickname = "Detail")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success|OK")})

    @GetMapping("/{recipeId}")
    public ResponseEntity<RecipeDto> detail(@PathVariable String recipeId) {

        return new ResponseEntity<>(recipeService.detail(recipeId), HttpStatus.OK);
    }

    @ApiOperation(value = "", notes = "<body>This rest service provide you to deelte recipe with id.</body>",
            nickname = "Delete")
    @DeleteMapping("/{recipeID}")
    public ResponseEntity<Void> delete(@PathVariable String recipeID) {

        recipeService.delete(recipeID);
        return new ResponseEntity<>(HttpStatus.OK);

    }

}
