package com.recipe.http.domain;

import java.sql.Array;
import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Ingredient {

    private String name;
    private Boolean isCommonAllergen;


    @JsonCreator
    public Ingredient(@JsonProperty("name") String name) {
        this.name = name;
        this.isCommonAllergen = compareIngredientWithCommonAllergensList();
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
