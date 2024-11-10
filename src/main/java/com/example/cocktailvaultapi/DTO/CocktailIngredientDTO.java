package com.example.cocktailvaultapi.DTO;

public class CocktailIngredientDTO {

    private String ingredientName; // Change from ingredientId to ingredientName
    private String quantity;
    private String spiritTypeName; // Add spirit type name

    /**
     * Getters and setters
     */
    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getSpiritTypeName() {
        return spiritTypeName;
    }

    public void setSpiritTypeName(String spiritTypeName) {
        this.spiritTypeName = spiritTypeName; // Ensure this method is correctly defined
    }
}
