package com.example.cocktailvaultapi.Model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "Cocktails")
public class Cocktail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cocktail_id")
    private Long id; // Match with cocktailId in DTO

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "instructions", columnDefinition = "TEXT")
    private String instructions;

    @ManyToOne
    @JoinColumn(name = "glass_type_id")
    private GlassType glassType;

    @ManyToOne
    @JoinColumn(name = "spirit_type_id")
    private SpiritType spiritType;

    @Column(name = "image_url", length = 255)
    private String imageUrl;

    @OneToMany(mappedBy = "cocktail", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CocktailIngredient> cocktailIngredients;

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

    public GlassType getGlassType() {
        return glassType;
    }

    public void setGlassType(GlassType glassType) {
        this.glassType = glassType;
    }

    public SpiritType getSpiritType() {
        return spiritType;
    }

    public void setSpiritType(SpiritType spiritType) {
        this.spiritType = spiritType;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<CocktailIngredient> getCocktailIngredients() {
        return cocktailIngredients;
    }

    public void setCocktailIngredients(List<CocktailIngredient> cocktailIngredients) {
        this.cocktailIngredients = cocktailIngredients;
    }

}
