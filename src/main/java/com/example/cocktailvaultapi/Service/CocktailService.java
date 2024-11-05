package com.example.cocktailvaultapi.Service;

import com.example.cocktailvaultapi.Model.Cocktail;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface CocktailService {
    List<Cocktail> findAllCocktails();
    Optional<Cocktail> findByName(String name);

}
