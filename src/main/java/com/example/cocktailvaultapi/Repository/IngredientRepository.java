package com.example.cocktailvaultapi.Repository;

import com.example.cocktailvaultapi.Model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    // Custom query methods can be defined here if needed
}
