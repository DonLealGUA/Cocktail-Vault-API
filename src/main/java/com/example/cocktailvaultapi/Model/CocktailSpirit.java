package com.example.cocktailvaultapi.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "cocktail_spirits")
public class CocktailSpirit {

    @Id
    @Column(name = "cocktail_id")
    private Long cocktailId; // The ID of the cocktail

    @Id
    @Column(name = "spirit_type_id")
    private Long spiritTypeId; // The ID of the spirit type used in the cocktail

    @ManyToOne
    @JoinColumn(name = "cocktail_id", insertable = false, updatable = false)
    private Cocktail cocktail; // Reference to the Cocktail entity

    @ManyToOne
    @JoinColumn(name = "spirit_type_id", insertable = false, updatable = false)
    private SpiritType spiritType; // Reference to the SpiritType entity

}
