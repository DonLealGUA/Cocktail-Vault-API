package com.example.cocktailvaultapi.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "spirit_types")
public class SpiritType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long spiritTypeId; // Unique identifier for the spirit type

    @Column(nullable = false, length = 50)
    private String name; // Name of the spirit type (e.g., "Vodka", "Rum")

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
