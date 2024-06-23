
package com.recipe.http.cli;

import com.recipe.http.client.RESTClient;
import com.recipe.http.domain.Ingredient;
import com.recipe.http.domain.Recipe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
        HTTPRestCLIApplication cliApp = new HTTPRestCLIApplication();

        String serverURLRoot = "http://localhost:8080";
        RESTClient restClient = cliApp.getRestClient();
        restClient.setServerURL(serverURLRoot);
        cliApp.setRestClient(restClient);

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Recipe Application: Introduction");
            System.out.println("2. Add new user ");
            System.out.println("3. Add new recipe");
            System.out.println("4. Get all recipes");
            System.out.println("5. Get recipe by recipe name");
            System.out.println("6. Search recipes with user ingredient list");
            System.out.println("7. Search vegan recipes");
            System.out.println("8. Search recipes without common allergens");
            System.out.println("9. Delete recipe.");
            System.out.println("10. Exit");

            int userChoice = scanner.nextInt();
            scanner.nextLine();

            String userChoiceURL;
            switch (userChoice) {
                case 1:
                    userChoiceURL = serverURLRoot + "/recipe";
                    cliApp.getRestClient().getGETResponseFromHTTPRequest(userChoiceURL, "recipe");
                    break;

                case 2:
                    userChoiceURL = serverURLRoot + "/newUser";
                    break;

                case 3:
                    cliApp.addNewRecipe(scanner, serverURLRoot);
                    break;

                case 4:
                    userChoiceURL = serverURLRoot + "/recipes";
                    cliApp.getRestClient().getGETResponseFromHTTPRequest(userChoiceURL, "recipes");
                    break;

                case 5:
                    System.out.println("Enter the recipe name: ");
                    String recipeSearched = scanner.nextLine();
                    userChoiceURL = serverURLRoot + "/recipe/" + recipeSearched;
                    cliApp.getRestClient().getGETResponseFromHTTPRequest(userChoiceURL, "recipe/{recipeName}");
                    break;

                case 6:
                    System.out.println("Enter the user ID: ");
                    int userID = scanner.nextInt();
                    cliApp.getRestClient().getRecipesForUserIngredients(userID);
                    break;

                case 7:
                    System.out.println("add");
                    break;

                case 8:
                    System.out.println("add");
                    break;

                case 9:
                    System.out.println("Enter the recipe name: ");
                    String recipeToDelete = scanner.nextLine().replaceAll(" ", "%20");
                    userChoiceURL = serverURLRoot + "/recipe/" + recipeToDelete;
                    cliApp.getRestClient().getDELETEResponseFromHTTPRequest(userChoiceURL, "/recipe/{recipeName}");
                    break;

                case 10:
                    scanner.close();
                    return;

                default:
                    System.out.println("Please enter a valid command (1 - 10).");
                    break;
            }

            if (cliApp.userReturnsToMainMenu(scanner)) {
                continue;
            }
        }
    }

    private void addNewRecipe(Scanner scanner, String serverURLRoot) throws IOException, InterruptedException {
        System.out.println("Enter the recipe name: ");
        String newRecipeName = scanner.nextLine();

        List<Ingredient> newRecipeIngredients = new ArrayList<>();
        while (true) {
            System.out.println("Enter an ingredient (type 'done' to quit): ");
            String recipeIngredientName = scanner.nextLine();
            if (recipeIngredientName.equalsIgnoreCase("done")) {
                break;
            }
            Ingredient nextIngredient = new Ingredient(recipeIngredientName);
            newRecipeIngredients.add(nextIngredient);
        }

        System.out.println("Enter the recipe instructions: ");
        String newRecipeInstructions = scanner.nextLine();

        Recipe newRecipe = new Recipe();
        newRecipe.setName(newRecipeName);
        newRecipe.setIngredients(newRecipeIngredients);
        newRecipe.setInstructions(newRecipeInstructions);

        String userChoiceURL = serverURLRoot + "/newRecipe";
        getRestClient().getPOSTResponseFromHTTPRequest(userChoiceURL, newRecipe);
    }

    private boolean userReturnsToMainMenu(Scanner scanner) {
        System.out.println("Press R to return to the main menu.");
        String returnKey = scanner.next();
        return returnKey.equalsIgnoreCase("r");
    }
}
