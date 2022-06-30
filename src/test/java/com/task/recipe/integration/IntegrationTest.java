package com.task.recipe.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.recipe.controller.RecipeController;
import com.task.recipe.dto.IngredientDto;
import com.task.recipe.dto.RecipeDto;
import com.task.recipe.service.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(MockitoExtension.class)
public class IntegrationTest {

    @InjectMocks
    private RecipeController recipeController;

    @Mock
    private RecipeService service;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(recipeController).build();
    }

    @Test
    public void addRecipe() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        when(service.save(any())).thenReturn("someid");

        RecipeDto recipeDto = createDummyRecipe();


        String jsonBody = objectMapper.writeValueAsString(recipeDto);

        // then
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/recipe")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonBody)
                )
                .andExpect(status().isCreated())
                .andExpect(header().stringValues("location", "/recipe/someid"));
    }


    @Test
    public void detail() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String dummyId = "dummy-id";

        RecipeDto recipeDto = createDummyRecipe();
        when(service.detail(dummyId)).thenReturn(recipeDto);

        // then
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/recipe/" + dummyId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andReturn();


        String expected = objectMapper.writeValueAsString(recipeDto);

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }

    @Test
    public void deleteRecipe() throws Exception {

        String dummyId = "dummy-id";

        // when
        mockMvc.perform(MockMvcRequestBuilders.delete("/recipe/" + dummyId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // then
        verify(service).delete(dummyId);
        verify(service, times(1)).delete(dummyId);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(service).delete(captor.capture());
        assertEquals(dummyId, captor.getValue());
    }


    @Test
    public void filter() throws Exception {

        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/recipe")
                        .param("vegi", "true")
                        .param("serveNum", "1")
                        .param("incIngr", "aple,banana")
                        .param("excIngr", "milk")
                        .param("instr", "oven")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());

        verify(service).filter(true,1,new String[]{"aple","banana"},new String[]{"milk"},"oven");
        verify(service, times(1)).filter(true,1,new String[]{"aple","banana"},new String[]{"milk"},"oven");;

    }


    private RecipeDto createDummyRecipe() {
        RecipeDto recipeDto = new RecipeDto(null,
                "browni",
                true,
                3,
                new ArrayList(List.of(
                        new IngredientDto("cacao", 50.0, "g"),
                        new IngredientDto("flour", 200.0, "g"),
                        new IngredientDto("coco mil", 0.1, "lt"),
                        new IngredientDto("banana", 300.0, "g")
                )),
                "smash banana, then add cacao and coco mil then and flour");
        return recipeDto;
    }
}
