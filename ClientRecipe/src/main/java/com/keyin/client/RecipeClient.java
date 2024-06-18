package com.keyin.client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.keyin.users.User;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RecipeClient {
    private static final String SERVER_URL = "http://localhost:8080/recipe";

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("1. Enter ingredients and find matching recipes");
            System.out.println("2. Exit");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    searchRecipesByIngredients(scanner);
                    break;
                case 2:
                    System.exit(0);
            }
        }
    }

    private static void searchRecipesByIngredients(Scanner scanner) throws IOException {
        List<Ingredient> ingredients = new ArrayList<>();
        while (true) {
            System.out.print("Enter ingredient name (or 'done' to finish): ");
            String name = scanner.nextLine();
            if (name.equalsIgnoreCase("done")) break;
            System.out.print("Is this a common allergen? (true/false): ");
            Boolean isCommonAllergen = scanner.nextBoolean();
            scanner.nextLine();  // Consume newline

            ingredients.add(new Ingredient(name, isCommonAllergen));
        }

        User user = new User();
        user.setUserCurrentIngredients(ingredients);

        URL url = new URL(SERVER_URL + "/userMatches");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; utf-8");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);

        String jsonInputString = new Gson().toJson(user);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        printResponse(conn);
    }

    private static void printResponse(HttpURLConnection conn) throws IOException {
        if (conn.getResponseCode() == 200) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }

                List<Recipe> recipes = new Gson().fromJson(response.toString(), new TypeToken<List<Recipe>>(){}.getType());
                if (recipes.isEmpty()) {
                    System.out.println("No matching recipes found.");
                } else {
                    System.out.println("Matching recipes:");
                    for (Recipe recipe : recipes) {
                        System.out.println(recipe);
                    }
                }
            }
        } else {
            System.out.println("Error: " + conn.getResponseMessage());
        }
    }
}

