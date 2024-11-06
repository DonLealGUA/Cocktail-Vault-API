package com.example.cocktailvaultapi.Service;

import com.example.cocktailvaultapi.DTO.CocktailDTO;
import com.example.cocktailvaultapi.DTO.PaginatedResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface CocktailService {
    List<CocktailDTO> searchAllCocktailsRecipes();
    PaginatedResponseDTO<CocktailDTO> searchAllCocktailsRecipesPageLimit(int page, int size);

    ResponseEntity<CocktailDTO> searchByNameIgnoreCase(String name); // Update return type to ResponseEntity
    List<CocktailDTO> listCocktailsByFirstLetter(char letter); // List all cocktails starting with a specific letter

}
