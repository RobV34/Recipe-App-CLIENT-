package com.recipe.http.domain;

import com.recipe.http.domain.Ingredient;

import java.util.List;

public class User {
    private int userId;
    private String firstName;
    private String lastName;
    private List<Ingredient> userCurrentIngredients;

    public User() {

    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    public List<Ingredient> getUserCurrentIngredients() {
        return userCurrentIngredients;
    }

    public void setUserCurrentIngredients(List<Ingredient> userCurrentIngredients) {
        this.userCurrentIngredients = userCurrentIngredients;
    }

    @Override
    public String toString() {
        return "User " + this.userId + ": " + this.firstName + " " + this.lastName + this.userCurrentIngredients;
    }
}
