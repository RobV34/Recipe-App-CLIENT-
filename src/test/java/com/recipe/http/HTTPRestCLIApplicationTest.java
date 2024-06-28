package com.recipe.http;

import com.recipe.http.cli.HTTPRestCLIApplication;
import com.recipe.http.client.RESTClient;
import com.recipe.http.domain.Ingredient;
import com.recipe.http.domain.Recipe;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class HTTPRestCLIApplicationTest {

    @Mock
    private RESTClient restClientMock;

    @Test
    void testAddNewRecipe_UserInput() throws IOException, InterruptedException {
        MockitoAnnotations.openMocks(this);
        HTTPRestCLIApplication cliApp = new HTTPRestCLIApplication();
        cliApp.setRestClient(restClientMock);


        String input = "Lasagna\nBeef\nCheese\ndone\nLayer everything and bake.";
        Scanner scanner = new Scanner(input);


        Recipe mockRecipe = new Recipe();
        mockRecipe.setName("Lasagna");
        mockRecipe.setIngredients(List.of(new Ingredient("Beef"), new Ingredient("Cheese")));
        mockRecipe.setInstructions("Layer everything and bake.");

        when(restClientMock.getPOSTResponseFromHTTPRequest(any(), any()))
                .thenReturn("Recipe added successfully");


        // cliApp.addNewRecipe(scanner, "http://localhost:8080");


        verify(restClientMock, times(1)).getPOSTResponseFromHTTPRequest(any(), any());
    }

    @Test
    void testUserReturnsToMainMenu() {
        MockitoAnnotations.openMocks(this);
        HTTPRestCLIApplication cliApp = new HTTPRestCLIApplication();
        cliApp.setRestClient(restClientMock);


        String input = "r";
        Scanner scanner = new Scanner(input);


        boolean returnedToMainMenu = cliApp.userReturnsToMainMenu();


        assertTrue(returnedToMainMenu);
    }

    @Test
    void testGetAllRecipes() {
        MockitoAnnotations.openMocks(this);
        HTTPRestCLIApplication cliApp = new HTTPRestCLIApplication();
        cliApp.setRestClient(restClientMock);


        Recipe mockRecipe1 = new Recipe();
        mockRecipe1.setName("Lasagna");
        mockRecipe1.setIngredients(List.of(new Ingredient("Beef"), new Ingredient("Cheese")));
        mockRecipe1.setInstructions("Layer everything and bake.");

        Recipe mockRecipe2 = new Recipe();
        mockRecipe2.setName("Pizza");
        mockRecipe2.setIngredients(List.of(new Ingredient("Dough"), new Ingredient("Cheese"), new Ingredient("Tomato Sauce")));
        mockRecipe2.setInstructions("Roll out dough, add toppings, and bake.");

        List<Recipe> mockRecipes = List.of(mockRecipe1, mockRecipe2);

        when(restClientMock.getGETResponseFromHTTPRequest(anyString(), eq("recipes")))
                .thenReturn(mockRecipes.toString());


        cliApp.getRestClient().setServerURL("http://localhost:8080");
        cliApp.getRestClient().getGETResponseFromHTTPRequest("http://localhost:8080/recipes", "recipes");


        verify(restClientMock, times(1)).getGETResponseFromHTTPRequest("http://localhost:8080/recipes", "recipes");
    }

    @Test
    void testGetRecipeByName() {
        MockitoAnnotations.openMocks(this);
        HTTPRestCLIApplication cliApp = new HTTPRestCLIApplication();
        cliApp.setRestClient(restClientMock);

        String input = "Lasagna";
        Scanner scanner = new Scanner(input);


        Recipe mockRecipe = new Recipe();
        mockRecipe.setName("Lasagna");
        mockRecipe.setIngredients(List.of(new Ingredient("Beef"), new Ingredient("Cheese")));
        mockRecipe.setInstructions("Layer everything and bake.");

        when(restClientMock.getGETResponseFromHTTPRequest(anyString(), eq("recipe/{recipeName}")))
                .thenReturn(mockRecipe.toString());


        cliApp.getRestClient().setServerURL("http://localhost:8080");
        cliApp.getRestClient().getGETResponseFromHTTPRequest("http://localhost:8080/recipe/Lasagna", "recipe/{recipeName}");


        verify(restClientMock, times(1)).getGETResponseFromHTTPRequest("http://localhost:8080/recipe/Lasagna", "recipe/{recipeName}");
    }

    @Test
    void testGetAllRecipes_NoRecipesFound() {
        MockitoAnnotations.openMocks(this);
        HTTPRestCLIApplication cliApp = new HTTPRestCLIApplication();
        cliApp.setRestClient(restClientMock);


        when(restClientMock.getGETResponseFromHTTPRequest(anyString(), eq("recipes")))
                .thenReturn("[]"); // Empty list of recipes


        String recipesResponse = cliApp.getRestClient().getGETResponseFromHTTPRequest("http://localhost:8080/recipes", "recipes");


        verify(restClientMock, times(1)).getGETResponseFromHTTPRequest(eq("http://localhost:8080/recipes"), eq("recipes"));


        Assertions.assertEquals("[]", recipesResponse);
    }

    @Test
    void testGetRecipeByName_NotFound() {
        MockitoAnnotations.openMocks(this);
        HTTPRestCLIApplication cliApp = new HTTPRestCLIApplication();
        cliApp.setRestClient(restClientMock);


        String input = "NonExistingRecipe";
        Scanner scanner = new Scanner(input);


        when(restClientMock.getGETResponseFromHTTPRequest(anyString(), eq("recipe/{recipeName}")))
                .thenReturn("Recipe not found");


        String recipeResponse = cliApp.getRestClient().getGETResponseFromHTTPRequest("http://localhost:8080/recipe/NonExistingRecipe", "recipe/{recipeName}");


        verify(restClientMock, times(1)).getGETResponseFromHTTPRequest(eq("http://localhost:8080/recipe/NonExistingRecipe"), eq("recipe/{recipeName}"));


        Assertions.assertEquals("Recipe not found", recipeResponse);
    }

    @Test
    void testUserReturnsToMainMenu_EmptyInput() {
        MockitoAnnotations.openMocks(this);
        HTTPRestCLIApplication cliApp = new HTTPRestCLIApplication();
        cliApp.setRestClient(restClientMock);


        String input = "\n\nr";
        Scanner scanner = new Scanner(input);


        boolean returnedToMainMenu = cliApp.userReturnsToMainMenu();


        assertTrue(returnedToMainMenu);
    }


    @Test
    void testUserReturnsToMainMenu_InvalidInput() {
        MockitoAnnotations.openMocks(this);
        HTTPRestCLIApplication cliApp = new HTTPRestCLIApplication();
        cliApp.setRestClient(restClientMock);


        String input = "x\n";
        Scanner scanner = new Scanner(input);


        boolean returnedToMainMenu = cliApp.userReturnsToMainMenu();


        assertFalse(returnedToMainMenu);
    }

    @Test
    void testGetRecipeByName_EmptyResponse() {
        MockitoAnnotations.openMocks(this);
        HTTPRestCLIApplication cliApp = new HTTPRestCLIApplication();
        cliApp.setRestClient(restClientMock);


        String input = "NonExistingRecipe";
        Scanner scanner = new Scanner(input);


        when(restClientMock.getGETResponseFromHTTPRequest(anyString(), eq("recipe/{recipeName}")))
                .thenReturn("");


        String recipeResponse = cliApp.getRestClient().getGETResponseFromHTTPRequest("http://localhost:8080/recipe/NonExistingRecipe", "recipe/{recipeName}");


        verify(restClientMock, times(1)).getGETResponseFromHTTPRequest(eq("http://localhost:8080/recipe/NonExistingRecipe"), eq("recipe/{recipeName}"));


        Assertions.assertEquals("", recipeResponse);
    }


    @Test
    void testSearchRecipesWithUserIngredients() throws IOException, InterruptedException {
        MockitoAnnotations.openMocks(this);
        HTTPRestCLIApplication cliApp = new HTTPRestCLIApplication();
        cliApp.setRestClient(restClientMock);


        int userId = 123;


        List<Recipe> mockRecipes = new ArrayList<>();
        Recipe recipe1 = new Recipe();
        recipe1.setName("Pasta");
        recipe1.setIngredients(List.of(new Ingredient("Pasta"), new Ingredient("Tomato Sauce")));
        recipe1.setInstructions("Boil pasta, add sauce, and serve.");
        Recipe recipe2 = new Recipe();
        recipe2.setName("Salad");
        recipe2.setIngredients(List.of(new Ingredient("Lettuce"), new Ingredient("Tomato"), new Ingredient("Cucumber")));
        recipe2.setInstructions("Chop vegetables, mix, and add dressing.");
        mockRecipes.add(recipe1);
        mockRecipes.add(recipe2);

        when(restClientMock.getRecipesForUserIngredients(userId))
                .thenReturn(mockRecipes);


        cliApp.getRestClient().setServerURL("http://localhost:8080");
        cliApp.getRestClient().getRecipesForUserIngredients(userId);


        verify(restClientMock, times(1)).getRecipesForUserIngredients(userId);
    }

    @Test
    void testDeleteRecipe() {
        MockitoAnnotations.openMocks(this);
        HTTPRestCLIApplication cliApp = new HTTPRestCLIApplication();
        cliApp.setRestClient(restClientMock);


        String recipeName = "Lasagna";


        when(restClientMock.getDELETEResponseFromHTTPRequest(anyString(), anyString()))
                .thenReturn("Recipe 'Lasagna' deleted successfully");


        cliApp.getRestClient().setServerURL("http://localhost:8080");
        cliApp.getRestClient().getDELETEResponseFromHTTPRequest("http://localhost:8080/recipe/Lasagna", "/recipe/{recipeName}");


        verify(restClientMock, times(1)).getDELETEResponseFromHTTPRequest("http://localhost:8080/recipe/Lasagna", "/recipe/{recipeName}");
    }

    @Test
    void testUpdateRecipeWithPOST() {
        MockitoAnnotations.openMocks(this);
        HTTPRestCLIApplication cliApp = new HTTPRestCLIApplication();
        cliApp.setRestClient(restClientMock);


        String input = "Lasagna\nBeef\nCheese\ndone\nLayer everything and bake.";
        Scanner scanner = new Scanner(input);


        when(restClientMock.getPOSTResponseFromHTTPRequest(anyString(), any()))
                .thenReturn("Recipe 'Lasagna' updated successfully");


        cliApp.getRestClient().setServerURL("http://localhost:8080");
        cliApp.getRestClient().getPOSTResponseFromHTTPRequest("http://localhost:8080/recipe/Lasagna", new Recipe("Lasagna", List.of(new Ingredient("Beef"), new Ingredient("Cheese")), "Layer everything and bake.", 3));


        verify(restClientMock, times(1)).getPOSTResponseFromHTTPRequest("http://localhost:8080/recipe/Lasagna", new Recipe("Lasagna", List.of(new Ingredient("Beef"), new Ingredient("Cheese")), "Layer everything and bake.", 3));
        
    }




}
