package com.keyin.client;

public class Ingredient {
    private String name;
    private Boolean isCommonAllergen;

    public Ingredient() {
    }

    public Ingredient(String name, Boolean isCommonAllergen) {
        this.name = name;
        this.isCommonAllergen = isCommonAllergen;
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

    @Override
    public String toString() {
        return "Ingredient{" +
                "name='" + name + '\'' +
                ", isCommonAllergen=" + isCommonAllergen +
                '}';
    }
}

