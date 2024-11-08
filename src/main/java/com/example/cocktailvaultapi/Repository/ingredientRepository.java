package com.example.cocktailvaultapi.Repository;

import com.example.cocktailvaultapi.Model.Ingredient;
import com.example.cocktailvaultapi.Model.SpiritType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ingredientRepository extends JpaRepository<Ingredient, Long>  {
    @Query("SELECT i.name FROM Ingredient i")
    List<String> findAllIngredientNames();


}
