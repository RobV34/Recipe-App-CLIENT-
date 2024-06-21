package com.recipe.http.cli;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.recipe.http.client.RESTClient;
import com.recipe.http.domain.Ingredient;
import com.recipe.http.domain.Recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import java.io.IOException;

public class HTTPRestCLIApplication {

    private RESTClient restClient;


    public RESTClient getRestClient() {
        if (restClient == null) {
            restClient = new RESTClient();
        }

        return restClient;
    }

    public void setRestClient(RESTClient restClient) {
        this.restClient = restClient;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        for (String arg : args) {
            System.out.println(arg);
        }

        HTTPRestCLIApplication cliApp = new HTTPRestCLIApplication();

        String serverURLRoot = "http://localhost:8080";


        if (serverURLRoot != null && !serverURLRoot.isEmpty()) {

            RESTClient restClient = new RESTClient();
            restClient.setServerURL(serverURLRoot);

            cliApp.setRestClient(restClient);


            Scanner scanner = new Scanner(System.in);

            System.out.println("1. Root Route");
            System.out.println("2. Add new user "); // code not added yet
            System.out.println("3. Add new recipe");
            System.out.println("4. Get all recipes");
            System.out.println("5. Get recipe by recipe name");
            System.out.println("6. Search recipes with user ingredient list");
            System.out.println("7. Search vegan recipes");
            System.out.println("8. Search recipes without common allergens");
            System.out.println("9. Delete recipe.");

            int userChoice = scanner.nextInt();
            scanner.nextLine();


            switch (userChoice) {
                case 1:
                    String userChoiceURL;
                    userChoiceURL = serverURLRoot + "/recipe";
                    cliApp.getRestClient().getGETResponseFromHTTPRequest(userChoiceURL, "recipe");
                    break;

                case 2:
                    userChoiceURL = serverURLRoot + "/newUser";
                case 3:
                    System.out.println("Enter the recipe name: ");
                    String newRecipeName = scanner.nextLine();

                    List<Ingredient> newRecipeIngredients = new ArrayList<>();
                    while (true) {
                        System.out.println("1. Enter an ingredient (done to quit): ");
                        String recipeIngredientName = scanner.nextLine();
                        Ingredient nextIngredient = new Ingredient(recipeIngredientName);

                        if (recipeIngredientName.equalsIgnoreCase("done")) {
                            break;
                        }
                        newRecipeIngredients.add(nextIngredient);

                        }

                    System.out.println("Enter the recipe instructions: ");
                    String newRecipeInstructions = scanner.nextLine();

                    Recipe newRecipe = new Recipe();
                    newRecipe.setName(newRecipeName);
                    newRecipe.setIngredients(newRecipeIngredients);
                    newRecipe.setInstructions(newRecipeInstructions);

                    userChoiceURL = serverURLRoot + "/newRecipe";
                    cliApp.getRestClient().getPOSTResponseFromHTTPRequest(userChoiceURL, newRecipe);
                    break;

                case 4:
                    userChoiceURL = serverURLRoot + "/recipes";
                    cliApp.getRestClient().getGETResponseFromHTTPRequest(userChoiceURL, "recipes");
                    break;

                case 5:
                    System.out.println("Enter the recipe name: ");
                    String recipeSearched = scanner.next();

                    userChoiceURL = serverURLRoot + "/recipe" + "/" + recipeSearched;
                    cliApp.getRestClient().getGETResponseFromHTTPRequest(userChoiceURL, "recipe/{recipeName}");
                    break;

                case 6:
                    System.out.println("Enter the user ID: ");
                    int userID = scanner.nextInt();

                    cliApp.getRestClient().getRecipesForUserIngredients(userID);
                    break;

                case 7:
                    System.out.println("add");
                case 8:
                    System.out.println("add");
                case 9:
                    System.out.println("Enter the recipe name: ");
                    String recipeToDelete = scanner.next();

                    userChoiceURL = serverURLRoot + "/recipe" + "/" + recipeToDelete;
                    cliApp.getRestClient().getDELETEResponseFromHTTPRequest(userChoiceURL, "/recipe/{recipeName}");
                    break;

                default:
                    System.out.println("default - switch clause");


            }


        }
    }
}
