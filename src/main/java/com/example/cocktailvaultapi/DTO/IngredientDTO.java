package com.example.cocktailvaultapi.DTO;

public class IngredientDTO {
    private String name;
    private String quantity;
    private boolean isLiqueur; // To differentiate liqueur types

    public IngredientDTO(String name, String quantity) {
        this.name = name;
        this.quantity = quantity;
        this.isLiqueur = isLiqueur;
    }


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

