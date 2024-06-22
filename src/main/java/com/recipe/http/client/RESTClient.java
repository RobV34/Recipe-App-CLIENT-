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
                case "recipes":
                    List<Recipe> allRecipesResponseBody = getAllRecipes(response.body());
                    return (T) allRecipesResponseBody;
                case "recipe/{recipeName}":
                    Recipe singleRecipeSearched = getRecipeByName(response.body());
                    return (T) singleRecipeSearched;
                default:
                    System.out.println("default");
                    return (T) "default";
            }


        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getStringResponse(String response) throws JsonProcessingException {

        ObjectMapper om = new ObjectMapper();
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        System.out.println(response);
        return response;
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

        System.out.println(allRecipes);
        return allRecipes;

    }


    public Recipe getRecipeByName(String response) throws JsonProcessingException {
        Recipe recipeSearched = new Recipe();
        TypeReference<Recipe> recipeTypeReference = new TypeReference<>() {};

        try {
            recipeSearched = configureAndReadValue(response, recipeTypeReference);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(recipeSearched);
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
                System.out.println("Status Code: " + response.statusCode());
            }

            List<Recipe> recipesForUser = om.readValue(response.body(), new TypeReference<List<Recipe>>() {});

            if (recipesForUser.size() == 0) {
                System.out.println("No recipes found with the list of ingredients given.");
            } else {
                System.out.println(recipesForUser);
            }

            return recipesForUser;

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }



    public <T> T getDELETEResponseFromHTTPRequest(String userChoiceURL, String requestParameter) {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(userChoiceURL)).DELETE().build();

        try {
            HttpResponse<String> response = getClient().send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode()!=200) {
                System.out.println("Status Code: " + response.statusCode());
            }

            System.out.println("Recipe deleted.");


        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }


    public <T> T getPOSTResponseFromHTTPRequest(String userChoiceURL, Recipe newRecipe) {

        ObjectMapper om = new ObjectMapper();
        String requestBody = "";

        try {
            requestBody = om.writeValueAsString(newRecipe);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(userChoiceURL)).header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(requestBody)).build();

        try {
            HttpResponse<String> response = getClient().send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode()!=200) {
                System.out.println("Status Code: " + response.statusCode());
            }

            System.out.println("New recipe added.");

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




}
