package com.example.cocktailvaultapi.Model;

import jakarta.persistence.*;
import java.util.List;


@Entity
@Table(name = "glass_types")
public class GlassType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long glassTypeId; // Unique identifier for the glass type

    @Column(nullable = false, length = 50)
    private String name; // Name of the glass type (e.g., "Martini Glass")

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
