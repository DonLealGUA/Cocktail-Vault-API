package com.example.cocktailvaultapi.Repository;

import com.example.cocktailvaultapi.Model.SpiritType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpiritRepository extends JpaRepository<SpiritType, Long> {
    List<SpiritType> findAll(); // Fetch all spirit types
}