package com.example.cocktailvaultapi.Service;

import com.example.cocktailvaultapi.DTO.CocktailDTO;
import com.example.cocktailvaultapi.DTO.PaginatedResponseDTO;
import com.example.cocktailvaultapi.Model.Cocktail;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public interface CocktailService {
    List<CocktailDTO> searchAllCocktailsRecipes();
    PaginatedResponseDTO<CocktailDTO> searchAllCocktailsRecipesPageLimit(int page, int size);
    ResponseEntity<CocktailDTO> getRandomCocktail(); // Get a random cocktail
    List<CocktailDTO> listLatestCocktails(int count); // List the latest cocktails added

    ResponseEntity<CocktailDTO> searchByNameIgnoreCase(String name); // Update return type to ResponseEntity
    List<CocktailDTO> listCocktailsByFirstLetter(char letter); // List all cocktails starting with a specific letter


    List<CocktailDTO> searchBySpiritBrand(String brand); // Get recipes by specific spirit brand (e.g., Absolut)
    List<CocktailDTO> searchBySpiritType(String spiritType); // Search cocktails by spirit type
    List<CocktailDTO> filterByGlassType(String glassType); // Filter cocktails by glass type


    List<CocktailDTO> searchBySpecificIngredient(String ingredient); // Search cocktails with a specific ingredient
    List<CocktailDTO> searchWithExactIngredients(List<String> ingredients, List<Integer> spirits) ; // Search cocktails with only specific ingredients

    List<CocktailDTO> searchWithPartialIngredients(List<String> normalizedIngredients, List<Integer> spiritTypeId);

}
