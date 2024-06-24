package com.recipe.http.cli;

import com.recipe.http.client.RESTClient;
import com.recipe.http.domain.Ingredient;
import com.recipe.http.domain.Recipe;
import com.recipe.http.domain.User;

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

            while(true) {

            System.out.println("1. Recipe Application: Introduction");
            System.out.println("2. Add new user "); // code not added yet
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


            switch (userChoice) {
                case 1:
                    String userChoiceURL;
                    userChoiceURL = serverURLRoot + "/recipe";
                    cliApp.getRestClient().getGETResponseFromHTTPRequest(userChoiceURL, "recipe");
                    if (cliApp.userReturnsToMainMenu()) {
                        break;
                    }

                case 2:
                    userChoiceURL = serverURLRoot + "/newUser";
                    System.out.println("Enter the user ID: ");
                    int userId = scanner.nextInt();
                    scanner.nextLine();

                    List<Ingredient> userIngredients = new ArrayList<>();
                    while (true) {
                        System.out.println("1. Enter an ingredient in your fridge/cupboard (done to quit): ");
                        String userIngredientName = scanner.nextLine();
                        Ingredient nextIngredient = new Ingredient(userIngredientName);

                        if (userIngredientName.equalsIgnoreCase("done")) {
                            break;
                        }
                        userIngredients.add(nextIngredient);
                    }

                    User newUser = new User();
                    newUser.setUserId(userId);
                    newUser.setUserCurrentIngredients(userIngredients);

                    userChoiceURL = serverURLRoot + "/newUser";
                    cliApp.getRestClient().getPOSTResponseFromHTTPRequest(userChoiceURL, newUser);


                    if (cliApp.userReturnsToMainMenu()) {
                        break;
                    }

                    System.out.println();
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

                    if (cliApp.userReturnsToMainMenu()) {
                        break;
                    }


                case 4:
                    userChoiceURL = serverURLRoot + "/recipes";
                    cliApp.getRestClient().getGETResponseFromHTTPRequest(userChoiceURL, "recipes");

                    if (cliApp.userReturnsToMainMenu()) {
                        break;
                    }


                case 5:
                    System.out.println("Enter the recipe name: ");
                    String recipeSearched = scanner.next();

                    userChoiceURL = serverURLRoot + "/recipe" + "/" + recipeSearched;
                    cliApp.getRestClient().getGETResponseFromHTTPRequest(userChoiceURL, "recipe/{recipeName}");

                    if (cliApp.userReturnsToMainMenu()) {
                        break;
                    }


                case 6:
                    System.out.println("Enter the user ID: ");
                    int userID = scanner.nextInt();

                    cliApp.getRestClient().getRecipesForUserIngredients(userID);

                    if (cliApp.userReturnsToMainMenu()) {
                        break;
                    }

                case 7:
                    System.out.println("add");
                case 8:
                    userChoiceURL = serverURLRoot + "/recipe/noCommonAllergens";
                    cliApp.getRestClient().getGETResponseFromHTTPRequest(userChoiceURL, "recipe/noCommonAllergens");

                    if (cliApp.userReturnsToMainMenu()) {
                        break;
                    }

                case 9:
                    System.out.println("Enter the recipe name: ");
                    String recipeToDelete = scanner.nextLine().replaceAll(" ", "%20");

                    userChoiceURL = serverURLRoot + "/recipe" + "/" + recipeToDelete;
                    cliApp.getRestClient().getDELETEResponseFromHTTPRequest(userChoiceURL, "/recipe/{recipeName}");

                    if (cliApp.userReturnsToMainMenu()) {
                        break;
                    }

                case 10:
                    System.out.println("Thank you for using the Recipe App.");
                    scanner.close();
                    return;
                default:
                    System.out.println("Please enter a valid command (1 - 10).");

            }
            }
            }
        }


    public Boolean userReturnsToMainMenu() {
        System.out.println("Press R to return to the main menu.");

        Scanner scanner = new Scanner(System.in);
        String returnKey = scanner.next();
        if (returnKey.equalsIgnoreCase("r")) {
            return true;
        }
        else {
            return false;
        }
    }

}
