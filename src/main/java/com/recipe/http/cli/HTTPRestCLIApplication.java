package com.recipe.http.cli;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.recipe.http.client.RESTClient;
import com.recipe.http.domain.Ingredient;
import com.recipe.http.domain.Recipe;
import com.recipe.http.domain.User;

import java.util.ArrayList;
import java.util.InputMismatchException;
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

            while (true) {
                System.out.println("1. Recipe Application: Introduction");
            System.out.println("2. Add new user ");
            System.out.println("3. Add new recipe");
            System.out.println("4. Get all recipes");
            System.out.println("5. Get recipe by recipe name");
            System.out.println("6. Search recipes with user ingredient list");
            System.out.println("7. Search recipes without common allergens");
            System.out.println("8. Delete recipe");
            System.out.println("9. Exit");

                int userChoice = 0;
                try {
                    userChoice = scanner.nextInt();
                    scanner.nextLine(); // consume newline
                } catch (InputMismatchException e) {
                    System.out.println("Please enter a valid number (1-10).");
                    scanner.next(); // clear the invalid input
                    continue;
                }

                switch (userChoice) {
                    case 1:
                        String userChoiceURL = serverURLRoot + "/recipe";
                        cliApp.getRestClient().getGETResponseFromHTTPRequest(userChoiceURL, "recipe");
                        if (cliApp.userReturnsToMainMenu()) {
                            break;
                        }
                        continue;

                    case 2:
                    userChoiceURL = serverURLRoot + "/newUser";
                    System.out.println("Enter the user ID: ");
                    int userId = scanner.nextInt();
                    scanner.nextLine();

                    System.out.println("Enter the first name: ");
                    String firstName = scanner.nextLine();

                    System.out.println("Enter the last name: ");
                    String lastName = scanner.nextLine();

                    List<Ingredient> userIngredients = new ArrayList<>();
                    while (true) {
                        System.out.println("Enter an ingredient in your fridge/cupboard (done to quit): ");
                        String userIngredientName = scanner.nextLine();
                        Ingredient nextIngredient = new Ingredient(userIngredientName);

                        if (userIngredientName.equalsIgnoreCase("done")) {
                            break;
                        }
                        userIngredients.add(nextIngredient);
                    }

                    User newUser = new User();
                    newUser.setUserId(userId);
                    newUser.setFirstName(firstName);
                    newUser.setLastName(lastName);
                    newUser.setUserCurrentIngredients(userIngredients);

                    userChoiceURL = serverURLRoot + "/newUser";
                    cliApp.getRestClient().getPOSTResponseFromHTTPRequest(userChoiceURL, newUser);


                    if (cliApp.userReturnsToMainMenu()) {
                        break;
                    }

                    continue; 

                    case 3:
                        System.out.println("Enter the recipe name: ");
                        String newRecipeName = scanner.nextLine();
                        if (newRecipeName.isEmpty()) {
                            System.out.println("Recipe name cannot be empty.");
                            break;
                        }

                        List<Ingredient> newRecipeIngredients = new ArrayList<>();
                        while (true) {
                            System.out.println("Enter an ingredient (type 'done' to finish): ");
                            String recipeIngredientName = scanner.nextLine();
                            if (recipeIngredientName.equalsIgnoreCase("done")) {
                                break;
                            }
                            if (recipeIngredientName.isEmpty()) {
                                System.out.println("Ingredient name cannot be empty.");
                                continue;
                            }
                            Ingredient nextIngredient = new Ingredient(recipeIngredientName);
                            newRecipeIngredients.add(nextIngredient);
                        }

                        System.out.println("Enter the recipe instructions: ");
                        String newRecipeInstructions = scanner.nextLine();
                        if (newRecipeInstructions.isEmpty()) {
                            System.out.println("Recipe instructions cannot be empty.");
                            break;
                        }

                        Recipe newRecipe = new Recipe();
                        newRecipe.setName(newRecipeName);
                        newRecipe.setIngredients(newRecipeIngredients);
                        newRecipe.setInstructions(newRecipeInstructions);

                        userChoiceURL = serverURLRoot + "/newRecipe";
                        cliApp.getRestClient().getPOSTResponseFromHTTPRequest(userChoiceURL, newRecipe);

                        if (cliApp.userReturnsToMainMenu()) {
                            break;
                        }
                        continue;

                    case 4:
                        userChoiceURL = serverURLRoot + "/recipes";
                        cliApp.getRestClient().getGETResponseFromHTTPRequest(userChoiceURL, "recipes");

                        if (cliApp.userReturnsToMainMenu()) {
                            break;
                        }
                        continue;

                    case 5:
                        System.out.println("Enter the recipe name: ");
                        String recipeSearched = scanner.nextLine();
                        if (recipeSearched.isEmpty()) {
                            System.out.println("Recipe name cannot be empty.");
                            break;
                        }

                        userChoiceURL = serverURLRoot + "/recipe/" + recipeSearched.replaceAll(" ", "%20");
                        cliApp.getRestClient().getGETResponseFromHTTPRequest(userChoiceURL, "recipe/{recipeName}");

                        if (cliApp.userReturnsToMainMenu()) {
                            break;
                        }
                        continue;

                    case 6:
                        System.out.println("Enter the user ID: ");
                        int userID = 0;
                        try {
                            userID = scanner.nextInt();
                            scanner.nextLine(); // consume newline
                            if (userID <= 0) {
                                System.out.println("User ID must be a positive integer.");
                                break;
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("Please enter a valid positive integer for the user ID.");
                            scanner.next(); // clear the invalid input
                            break;
                        }

                        cliApp.getRestClient().getRecipesForUserIngredients(userID);

                        if (cliApp.userReturnsToMainMenu()) {
                            break;
                        }
                        continue;

                    case 7:
                       userChoiceURL = serverURLRoot + "/recipe/noCommonAllergens";
                    cliApp.getRestClient().getGETResponseFromHTTPRequest(userChoiceURL, "recipe/noCommonAllergens");

                    if (cliApp.userReturnsToMainMenu()) {
                        break;
                    }
                        continue;

                    case 8:
                        System.out.println("Enter the recipe name: ");
                        String recipeToDelete = scanner.nextLine().replaceAll(" ", "%20");
                        if (recipeToDelete.isEmpty()) {
                            System.out.println("Recipe name cannot be empty.");
                            break;
                        }

                        userChoiceURL = serverURLRoot + "/recipe/" + recipeToDelete;
                        cliApp.getRestClient().getDELETEResponseFromHTTPRequest(userChoiceURL, "/recipe/{recipeName}");

                        if (cliApp.userReturnsToMainMenu()) {
                            break;
                        }
                        continue;

                    case 9:
                        scanner.close();
                        return;

                    default:
                        System.out.println("Please enter a valid command (1 - 9).");
                }
            }
        }
    }

    public Boolean userReturnsToMainMenu() {
        System.out.println("Press R to return to the main menu.");
        Scanner scanner = new Scanner(System.in);
        String returnKey = scanner.next();
        return returnKey.equalsIgnoreCase("r");
    }
}
