package com.example.cocktailvaultapi.Repository;

import com.example.cocktailvaultapi.Model.Cocktail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CocktailRepository extends JpaRepository<Cocktail, Long> {

    // Custom query method
     // Get all cocktails with pagination (max 32 per page)

    Optional<Cocktail> findByNameIgnoreCase(String name); // Search for cocktails by name
    List<Cocktail> findByNameStartingWithIgnoreCase(String letter); // List all cocktails starting with a specific letter



}
