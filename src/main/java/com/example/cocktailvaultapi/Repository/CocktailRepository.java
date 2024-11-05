package com.example.cocktailvaultapi.Repository;

import com.example.cocktailvaultapi.Model.Cocktail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CocktailRepository extends JpaRepository<Cocktail, Long> {
    Optional<Cocktail> findByName(String name); // Custom query method


}
