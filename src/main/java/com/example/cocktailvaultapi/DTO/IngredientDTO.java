package com.example.cocktailvaultapi.DTO;

public class IngredientDTO {
    private String name;
    private String quantity;

    /**
     * Getter and setter
     */

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}

