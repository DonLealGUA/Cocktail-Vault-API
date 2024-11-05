package com.example.cocktailvaultapi.DTO;

import java.util.List;

public class CocktailDTO {
    private Long id;
    private String name;
    private String instructions;
    private String imageUrl;
    private String glassType;
    private String spiritType;
    private List<CocktailIngredientDTO> ingredients; // Change here

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getGlassType() {
        return glassType;
    }

    public void setGlassType(String glassType) {
        this.glassType = glassType;
    }

    public String getSpiritType() {
        return spiritType;
    }

    public void setSpiritType(String spiritType) {
        this.spiritType = spiritType;
    }

    public List<CocktailIngredientDTO> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<CocktailIngredientDTO> ingredients) {
        this.ingredients = ingredients;
    }
}
