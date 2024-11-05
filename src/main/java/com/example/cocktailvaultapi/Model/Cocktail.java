package com.example.cocktailvaultapi.Model;

import jakarta.persistence.*;
import java.util.List;
import java.util.Date;

@Entity
@Table(name = "cocktails")
public class Cocktail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cocktail_id")
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "instructions", columnDefinition = "TEXT")
    private String instructions;

    @ManyToOne
    @JoinColumn(name = "glass_type_id")
    private GlassType glassType;

    @OneToMany(mappedBy = "cocktail", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CocktailSpirit> cocktailSpirits; // Updated to hold multiple spirits

    @Column(name = "image_url", length = 255)
    private String imageUrl;

    @OneToMany(mappedBy = "cocktail", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CocktailIngredient> cocktailIngredients;

    @Column(name = "created_by", nullable = false, length = 100)
    private String createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date", nullable = false, updatable = false)
    private Date createdDate;

    @Column(name = "spirit_brand", nullable = true, length = 100)
    private String spiritBrand = "None";

    @PrePersist
    public void prePersist() {
        this.createdDate = new Date();
    }

    // Getters and setters
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

    public List<CocktailSpirit> getCocktailSpirits() {
        return cocktailSpirits;
    }

    public void setCocktailSpirits(List<CocktailSpirit> cocktailSpirits) {
        this.cocktailSpirits = cocktailSpirits;
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getSpiritBrand() {
        return spiritBrand;
    }

    public void setSpiritBrand(String spiritBrand) {
        this.spiritBrand = spiritBrand;
    }
}
