package com.example.cocktailvaultapi.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "cocktail_ingredients")
public class CocktailIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Unique identifier for the cocktail ingredient relationship

    @ManyToOne
    @JoinColumn(name = "cocktail_id")
    private Cocktail cocktail; // Reference to the Cocktail entity

    @ManyToOne
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient; // Reference to the Ingredient entity

    private String quantity; // The quantity of the ingredient used in the cocktail

    @ManyToOne
    @JoinColumn(name = "spirit_type_id")
    private SpiritType spiritType; // Reference to the SpiritType entity for the type of spirit used in the cocktail

    /**
     * Getters and setters
     */
    public Ingredient getIngredient() {
        return ingredient;
    }

    public String getQuantity() {
        return quantity;
    }

    public SpiritType getSpiritType() {
        return spiritType;
    }

}
