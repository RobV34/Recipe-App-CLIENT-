package com.recipe.http.domain;

import java.util.List;

public class Recipe {
    private String name;
    private List<Ingredient> ingredients;
    private String instructions;

    private int difficulty;

    public Recipe() {
    }

    public Recipe(String name, List<Ingredient> ingredients, String instructions, int difficulty) {
        this.name = name;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.difficulty = difficulty;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "name='" + name + '\'' +
                ", ingredients=" + ingredients +
                ", instructions='" + instructions + '\'' +
                ", difficulty='" + difficulty + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Recipe recipe = (Recipe) o;

        if (name != null ? !name.equals(recipe.name) : recipe.name != null) return false;
        if (ingredients != null ? !ingredients.equals(recipe.ingredients) : recipe.ingredients != null) return false;
        return instructions != null ? instructions.equals(recipe.instructions) : recipe.instructions == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (ingredients != null ? ingredients.hashCode() : 0);
        result = 31 * result + (instructions != null ? instructions.hashCode() : 0);
        return result;
    }
}
