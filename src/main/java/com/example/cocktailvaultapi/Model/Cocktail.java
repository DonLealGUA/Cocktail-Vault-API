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
    private Long cocktailId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "instructions", columnDefinition = "TEXT")
    private String instructions;

    @ManyToOne
    @JoinColumn(name = "glass_type_id") // The 'glass_type_id' column in the cocktail table
    private GlassType glassType; // This should reference the GlassType entity

    @OneToMany(mappedBy = "cocktail", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CocktailSpirit> cocktailSpirits;

    @Column(name = "image_url", length = 255)
    private String imageUrl;

    @OneToMany(mappedBy = "cocktail", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CocktailIngredient> cocktailIngredients;

    @Column(name = "created_by")
    private String createdBy;


    @Column(name = "created_by_link", nullable = true, length = 100)
    private String createdByLink;

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
    public Long getCocktailId() {
        return cocktailId;
    }

    public void setCocktailId(Long id) {
        this.cocktailId = id;
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

    public String getCreatedByLink() {
        return createdByLink;
    }

    public void setCreatedByLink(String createdByLink) {
        this.createdByLink = createdByLink;
    }
}
