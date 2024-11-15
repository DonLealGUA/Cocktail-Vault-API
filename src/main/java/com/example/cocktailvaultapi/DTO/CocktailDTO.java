package com.example.cocktailvaultapi.DTO;

import java.util.Date;
import java.util.List;

public class CocktailDTO {
    private Long id;
    private String name;
    private String instructions;
    private String imageUrl;
    private String glassType;
    private List<String> spiritTypes;
    private List<CocktailIngredientDTO> ingredients;
    private String createdBy;
    private String createdByLink;
    private Date createdDate;
    private String spiritBrand;
    private String iceForm;


    /**
     * Getters and setters
     */


    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public void setGlassType(String glassType) {
        this.glassType = glassType;
    }


    public void setSpiritTypes(List<String> spiritTypes) {
        this.spiritTypes = spiritTypes;
    }


    public void setIngredients(List<CocktailIngredientDTO> ingredients) {
        this.ingredients = ingredients;
    }


    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }


    public void setCreatedByLink(String createdByLink) {
        this.createdByLink = createdByLink;
    }


    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }


    public void setSpiritBrand(String spiritBrand) {
        this.spiritBrand = spiritBrand;
    }


    public void setIceForm(String iceForm) {
        this.iceForm = iceForm;
    }

    public Long getId() {
        return id;
    }

    public String getInstructions() {
        return instructions;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getGlassType() {
        return glassType;
    }

    public List<String> getSpiritTypes() {
        return spiritTypes;
    }

    public List<CocktailIngredientDTO> getIngredients() {
        return ingredients;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getCreatedByLink() {
        return createdByLink;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public String getSpiritBrand() {
        return spiritBrand;
    }

    public String getIceForm() {
        return iceForm;
    }
}
