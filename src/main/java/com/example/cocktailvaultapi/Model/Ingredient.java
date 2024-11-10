package com.example.cocktailvaultapi.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "Ingredients")
public class Ingredient {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ingredient_id")
    private Long ingredientId; // Unique identifier for the ingredient

    @Column(nullable = false, length = 100)
    private String name; // Name of the ingredient (e.g., "Lime")

    @Column(length = 50)
    private String type; // Type of ingredient (e.g., "Fruit", "Soda")

    /**
     * Getters and setters
     */

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
