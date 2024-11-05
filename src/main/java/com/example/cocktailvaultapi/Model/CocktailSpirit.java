package com.example.cocktailvaultapi.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "cocktail_spirits")
public class CocktailSpirit {

    @Id
    @Column(name = "cocktail_id")
    private Long cocktailId;

    @Id
    @Column(name = "spirit_type_id")
    private Long spiritTypeId;

    @ManyToOne
    @JoinColumn(name = "cocktail_id", insertable = false, updatable = false)
    private Cocktail cocktail;

    @ManyToOne
    @JoinColumn(name = "spirit_type_id", insertable = false, updatable = false)
    private SpiritType spiritType;

    // Getters and Setters
    public Long getCocktailId() {
        return cocktailId;
    }

    public void setCocktailId(Long cocktailId) {
        this.cocktailId = cocktailId;
    }

    public Long getSpiritTypeId() {
        return spiritTypeId;
    }

    public void setSpiritTypeId(Long spiritTypeId) {
        this.spiritTypeId = spiritTypeId;
    }

    public Cocktail getCocktail() {
        return cocktail;
    }

    public void setCocktail(Cocktail cocktail) {
        this.cocktail = cocktail;
    }

    public SpiritType getSpiritType() {
        return spiritType;
    }

    public void setSpiritType(SpiritType spiritType) {
        this.spiritType = spiritType;
    }
}
