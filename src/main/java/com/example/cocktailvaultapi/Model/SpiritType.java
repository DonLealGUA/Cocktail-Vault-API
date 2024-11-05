package com.example.cocktailvaultapi.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "spirit_types")
public class SpiritType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long spiritTypeId;

    @Column(nullable = false, length = 50)
    private String name;

    public Long getSpiritTypeId() {
        return spiritTypeId;
    }

    public void setSpiritTypeId(Long spiritTypeId) {
        this.spiritTypeId = spiritTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getters and Setters
}
