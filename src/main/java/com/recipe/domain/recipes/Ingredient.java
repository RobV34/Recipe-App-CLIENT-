package com.recipe.domain.recipes;

import java.sql.Array;
import java.util.Arrays;


public class Ingredient {

    private String name;
    private Boolean isCommonAllergen;

    public Ingredient() {
    }

    public Ingredient(String name) {
        this.name = name;
        if (compareIngredientWithCommonAllergensList()) {
            isCommonAllergen = true;
        } else {
            isCommonAllergen = false;
        }
    }

    public Boolean compareIngredientWithCommonAllergensList(){
        String[] commonAllergenList = {"peanuts", "milk", "eggs", "pecans", "walnuts", "soy", "almonds"};

        Boolean isOnAllergenList = false;

        for (String allergen : commonAllergenList) {
            if (allergen.equalsIgnoreCase(this.name)){
                isOnAllergenList = true;
            }
        }
        return isOnAllergenList;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getCommonAllergen() {
        return isCommonAllergen;
    }

    public void setCommonAllergen(Boolean commonAllergen) {
        isCommonAllergen = commonAllergen;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Ingredient that = (Ingredient) obj;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
