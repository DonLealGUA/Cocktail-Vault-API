package com.example.cocktailvaultapi.Model;

import jakarta.persistence.*;
import java.util.List;


@Entity
@Table(name = "glass_types")
public class GlassType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long glassTypeId;

    @Column(nullable = false, length = 50)
    private String name;

    public Long getGlassTypeId() {
        return glassTypeId;
    }

    public void setGlassTypeId(Long glassTypeId) {
        this.glassTypeId = glassTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getters and Setters
}
