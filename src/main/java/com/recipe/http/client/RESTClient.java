package com.recipe.http.client;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.recipe.http.domain.Ingredient;
import com.recipe.http.domain.Recipe;
import com.recipe.http.domain.User;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RESTClient {

    private String serverURL;
    private HttpClient client;



    public <T> T getGETResponseFromHTTPRequest(String userChoiceURL, String requestParameter) {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(userChoiceURL)).build();

        try {
            HttpResponse<String> response = getClient().send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode()!=200) {
                System.out.println("Status Code: " + response.statusCode());
            }

            switch (requestParameter) {
                case "recipe":
                    String responseBody = getStringResponse(response.body());
                    return (T) responseBody;
                case "recipes", "recipe/noCommonAllergens":
                    List<Recipe> allRecipesResponseBody = getAllRecipes(response.body());
                    generateFormattedRecipes(allRecipesResponseBody);
                    return (T) allRecipesResponseBody;
                case "recipe/{recipeName}":
                    Recipe singleRecipeSearched = getRecipeByName(response.body());

                    if (singleRecipeSearched == null) {
                        String noRecipeFoundMessage = "No recipe found with that name.";
                        System.out.println(noRecipeFoundMessage);
                        return (T) noRecipeFoundMessage;
                    } else {
                        generateSingleFormattedRecipe(singleRecipeSearched);
                        return (T) singleRecipeSearched;
                    }
                default:
                    System.out.println("No URL found.");
                    return (T) "No URL found.";
            }


        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getStringResponse(String response) throws JsonProcessingException {

        ObjectMapper om = new ObjectMapper();
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            System.out.println(response);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public List<Recipe> getAllRecipes(String response) throws JsonProcessingException {
        List<Recipe> allRecipes = new ArrayList<>();
        TypeReference<List<Recipe>> recipeListTypeReference = new TypeReference<List<Recipe>>() {
        };

       try {
           allRecipes = configureAndReadValue(response, recipeListTypeReference);
       } catch (Exception e) {
           e.printStackTrace();
       }

        return allRecipes;

    }


    public Recipe getRecipeByName(String response) throws JsonProcessingException {

        if (response == null || response.isEmpty()) {
            return null;
        }

        Recipe recipeSearched = new Recipe();
        TypeReference<Recipe> recipeTypeReference = new TypeReference<>() {};

        try {
            recipeSearched = configureAndReadValue(response, recipeTypeReference);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return recipeSearched;

    }



    public List<Recipe> getRecipesForUserIngredients(int userId) throws IOException, InterruptedException {

        ObjectMapper om = new ObjectMapper();
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        String urlWithUser = serverURL + "/recipe/userMatches?userId=" + userId;

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(urlWithUser)).build();

        try {
            HttpResponse<String> response = getClient().send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {

                String errorMessage = getErrMessageFromResponse(response.body());

                if (errorMessage.contains("\"selectedUser\" is null")) {
                    System.out.println("User with ID " + userId + " was not found.");
                    return null;
                }

                System.out.println("Status Code: " + response.statusCode());

            } else {

                List<Recipe> recipesForUser = om.readValue(response.body(), new TypeReference<List<Recipe>>() {
                });

                if (recipesForUser.size() == 0) {
                    System.out.println("No recipes found with the list of ingredients given.");
                } else {
                    generateFormattedRecipes(recipesForUser);
                }

                return recipesForUser;
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }



    public String getDELETEResponseFromHTTPRequest(String userChoiceURL, String requestParameter) {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(userChoiceURL)).DELETE().build();

        try {
            HttpResponse<String> response = getClient().send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                System.out.println(response.body());
                return response.body();
            } else {
                System.out.println("Error: " + response.statusCode());
                return "Error deleting recipe";
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "Exception occurred while deleting recipe";
        }

    }



    public <T> T getPOSTResponseFromHTTPRequest(String userChoiceURL, T newObjectFromUser) {

        ObjectMapper om = new ObjectMapper();
        String requestBody = "";

        try {
            requestBody = om.writeValueAsString(newObjectFromUser);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(userChoiceURL)).header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(requestBody)).build();

        try {
            HttpResponse<String> response = getClient().send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode()!=200) {
                System.out.println("Status Code: " + response.statusCode());
            }

            if (newObjectFromUser instanceof Recipe) {
                TypeReference<Recipe> typeReference = new TypeReference<Recipe>() {};
                System.out.println("New recipe added: " + ((Recipe) newObjectFromUser).getName());
                return (T) configureAndReadValue(response.body(), typeReference);
            } else {

                TypeReference<User> typeReference = new TypeReference<User>() {};
                System.out.println("New user added with ID: " + ((User) newObjectFromUser).getUserId());
                return (T) configureAndReadValue(response.body(), typeReference);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }



    public String getServerURL() {
        return serverURL;
    }

    public void setServerURL(String serverURL) {
        this.serverURL = serverURL;
    }

    public HttpClient getClient() {
        if (client == null) {
            client  = HttpClient.newHttpClient();
        }

        return client;
    }


    public <T> T configureAndReadValue(String response, TypeReference<T> typeReference) throws JsonProcessingException {

        ObjectMapper om = new ObjectMapper();
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return om.readValue(response, typeReference);

    }



    private String getErrorMessageFromResponse(String response) {
        ObjectMapper om = new ObjectMapper();
        try {
            JsonNode rootNode = om.readTree(response);
            JsonNode messageNode = rootNode.path("message");
            return messageNode.asText();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }


        public void generateFormattedRecipes(List<Recipe> listOfRecipes) {

        if (listOfRecipes.size() < 1) {
            System.out.println("No recipes in the system.");
        }

        for(Recipe recipe : listOfRecipes ) {
            System.out.println(recipe.getName());
            System.out.print("Ingredients: ");
            for (Ingredient ingredient : recipe.getIngredients()) {
                System.out.print(ingredient + ", ");
            }
            System.out.println();
            System.out.println("Instructions: " + recipe.getInstructions());
            System.out.println("Difficulty Level: " + recipe.getDifficulty());
            System.out.println();
        }
        }

        public void generateSingleFormattedRecipe(Recipe recipe) {

            System.out.println(recipe.getName());
            System.out.print("Ingredients: ");
            for (Ingredient ingredient : recipe.getIngredients()) {
                System.out.print(ingredient + ", ");
            }
            System.out.println();
            System.out.println("Instructions: " + recipe.getInstructions());
            System.out.println("Difficulty Level: " + recipe.getDifficulty());
            System.out.println();

        }

    public String getErrMessageFromResponse(String response) {
        ObjectMapper om = new ObjectMapper();
        try {
            JsonNode rootNode = om.readTree(response);
            JsonNode messageNode = rootNode.path("message");
            return messageNode.asText();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }






}
