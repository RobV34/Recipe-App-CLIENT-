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


    //    Recipe
    public <T> T getGETResponseFromHTTPRequest(String requestParameter) {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(serverURL)).build();

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

        ObjectMapper om = new ObjectMapper();
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        allRecipes = om.readValue(response, new TypeReference<List<Recipe>>() {
        });

        System.out.println(allRecipes);
        return allRecipes;

    }


    public Recipe getRecipeByName(String response) throws JsonProcessingException {
        Recipe recipeSearched = new Recipe();

        ObjectMapper om = new ObjectMapper();
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        recipeSearched = om.readValue(response, new TypeReference<Recipe>() {});

        System.out.println(recipeSearched.getName());
        return recipeSearched;

    }

    public User getUserById(int userId) throws IOException, InterruptedException {
        User requestedUser = new User();

        String url = "http://localhost:8080/user/" + userId;

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();

        ObjectMapper om = new ObjectMapper();
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            System.out.println("Failed to fetch user. Status code: " + response.statusCode());
            return null;
        }
        requestedUser = om.readValue(response.body(), new TypeReference<User>() {});

        return requestedUser;
    }

    public List<Recipe> getRecipesForUserIngredients(int userId) throws IOException, InterruptedException {

        ObjectMapper om = new ObjectMapper();
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        User selectedUser = getUserById(userId);

        String urlWithUser = serverURL + "?userId=" + selectedUser.getUserId();

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(urlWithUser)).build();

        try {
            HttpResponse<String> response = getClient().send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                System.out.println("Status Code: " + response.statusCode());
            }

            // Deserialize the response body to a list of Recipe objects
            List<Recipe> recipesForUser = om.readValue(response.body(), new TypeReference<List<Recipe>>() {});

            System.out.println(recipesForUser);
            return recipesForUser;

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }



    public <T> T getDELETEResponseFromHTTPRequest(String requestParameter) {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(serverURL)).DELETE().build();

        try {
            HttpResponse<String> response = getClient().send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode()!=200) {
                System.out.println("Status Code: " + response.statusCode());
            }

            switch (requestParameter) {
                case "recipe/{recipeName}":
                    System.out.println(response.body());
                    return (T) response.body();
                default:
                    System.out.println("default");
                    return (T) "default";
            }


        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }



//    getPOSTResponseFromHTTP - recipes
//    getPUTResponseFromHTTP - recipes

//    getPOSTResponseFromUserHTTP - create user
//    getDELETEResponseFromUserHTTP ???



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







//    parse Json
//    private static JsonNode parse(String jsonSrc) throws JsonProcessingException {
//        return myObjectMapper.readTree(jsonSrc);
//    }
//
//
//    public static JsonNode toJson(Object object) {
//        return myObjectMapper.valueToTree(object);
//    }




}
