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
    private Long cocktailId; // Unique identifier for the cocktail

    @Column(name = "name", nullable = false, length = 100)
    private String name; // Name of the cocktail

    @Column(name = "instructions", columnDefinition = "TEXT")
    private String instructions; // Preparation instructions for the cocktail

    @ManyToOne
    @JoinColumn(name = "glass_type_id")
    private GlassType glassType; // Reference to the GlassType entity for the type of glass used

    @OneToMany(mappedBy = "cocktail", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CocktailSpirit> cocktailSpirits; // List of spirits used in the cocktail

    @Column(name = "image_url", length = 255)
    private String imageUrl; // URL for the cocktail's image

    @OneToMany(mappedBy = "cocktail", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CocktailIngredient> cocktailIngredients; // List of ingredients used in the cocktail

    @Column(name = "created_by")
    private String createdBy; // The person who created the cocktail recipe

    @Column(name = "ice_form")
    private String IceForm; // Type of ice used in the cocktail (e.g., crushed, cubes)

    @Column(name = "created_by_link", nullable = true, length = 100)
    private String createdByLink; // Link to the source or creator's profile

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date", nullable = false, updatable = false)
    private Date createdDate; // Date and time when the cocktail was created

    @Column(name = "spirit_brand", nullable = true, length = 100)
    private String spiritBrand = "None"; // The brand of spirit used in the cocktail (default is "None")

    @PrePersist
    public void prePersist() {
        this.createdDate = new Date();
    }

    /**
     * Getters and setters
     */
    public Long getCocktailId() {
        return cocktailId;
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

    public GlassType getGlassType() {
        return glassType;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public List<CocktailIngredient> getCocktailIngredients() {
        return cocktailIngredients;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public String getSpiritBrand() {
        return spiritBrand;
    }

    public String getCreatedByLink() {
        return createdByLink;
    }

    public String getIceForm() {
        return IceForm;
    }

}
