package com.recipe.http.domain;

import java.util.ArrayList;

public class RecipeManager {

    private String recipeManagerName;
    private String recipesType;
    private ArrayList<Recipe> recipeArrayList;



    public ArrayList<Recipe> getRecipeArrayList() {
        return recipeArrayList;
    }

    public void setRecipeArrayList(ArrayList<Recipe> recipeArrayList) {
        this.recipeArrayList = recipeArrayList;
    }

    public String getRecipesType() {
        return recipesType;
    }

    public void setRecipesType(String recipesType) {
        this.recipesType = recipesType;
    }

    public String getRecipeManagerName() {
        return recipeManagerName;
    }

    public void setRecipeManagerName(String recipeManagerName) {
        this.recipeManagerName = recipeManagerName;
    }


}
