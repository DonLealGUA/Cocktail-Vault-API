package com.example.cocktailvaultapi.Controller;

import com.example.cocktailvaultapi.DTO.CocktailDTO;
import com.example.cocktailvaultapi.DTO.PaginatedResponseDTO;
import com.example.cocktailvaultapi.Model.Cocktail;
import com.example.cocktailvaultapi.Service.CocktailService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cocktails")
public class CocktailController {

    private final CocktailService cocktailService; // Use the interface instead of the implementation

    public CocktailController(CocktailService cocktailService) {
        this.cocktailService = cocktailService;
    }

    /**
     * Default mapping.
     */
    @GetMapping
    public List<CocktailDTO> getAllCocktails() {
        return cocktailService.searchAllCocktailsRecipes();
    }

    /**
     * /cocktails/page?page=0&size=10
     */
    @GetMapping("/page")
    public ResponseEntity<PaginatedResponseDTO<CocktailDTO>> getAllCocktailsPageLimit(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        // Call the service method to get the paginated response
        PaginatedResponseDTO<CocktailDTO> response = cocktailService.searchAllCocktailsRecipesPageLimit(page, size);

        // Return the paginated response
        return ResponseEntity.ok(response);
    }

    /**
     * latest?count=5
     */
    @GetMapping("/latest")
    public List<CocktailDTO> listLatestCocktails(@RequestParam(defaultValue = "5") int count) {
        return cocktailService.listLatestCocktails(count);
    }

    @GetMapping("/random")
    public ResponseEntity<CocktailDTO> getRandomCocktail() {
        return cocktailService.getRandomCocktail();
    }



    @GetMapping("/{name}")
    public ResponseEntity<CocktailDTO> getCocktailByName(@PathVariable("name") String name) {
        return cocktailService.searchByNameIgnoreCase(name);
    }

    @GetMapping("/by-letter/{letter}")
    public List<CocktailDTO> listCocktailsByFirstLetter(@PathVariable char letter) {
        return cocktailService.listCocktailsByFirstLetter(letter);
    }


    @GetMapping("/filter/brand/{brand}")
    public List<CocktailDTO> getCocktailsBySpiritBrand(@PathVariable("brand") String brand) {
        return cocktailService.searchBySpiritBrand(brand);
    }

    @GetMapping("/filter/spirit/{spirit}")
    public List<CocktailDTO> filterBySpiritType(@PathVariable("spirit") String spiritType) {
        return cocktailService.searchBySpiritType(spiritType);
    }
    @GetMapping("/filter/glass/{glassType}")
    public List<CocktailDTO> filterByGlassType(@PathVariable("glassType") String glassType) {
        return cocktailService.filterByGlassType(glassType);
    }


    @GetMapping("/filter/ingredient/{ingredient}")
    public List<CocktailDTO> searchBySpecificIngredient(@PathVariable("ingredient") String ingredient) {
        return cocktailService.searchBySpecificIngredient(ingredient);
    }


    @GetMapping("/ingredients/exact")
    public ResponseEntity<Object> searchWithExactIngredients(
            @RequestParam List<String> ingredients,
            @RequestParam(required = false) List<String> spirit_types) {

        // Validate the ingredients parameter
        if (ingredients == null || ingredients.isEmpty()) {
            return ResponseEntity.badRequest().body("No ingredients provided.");
        }

        // If spirit_types is null, default it to an empty list
        if (spirit_types == null) {
            spirit_types = new ArrayList<>();
        }

        // Search for exact matches
        List<CocktailDTO> cocktails = cocktailService.searchWithExactIngredients(ingredients, spirit_types);

        if (cocktails.isEmpty()) {
            return ResponseEntity.ok("No exact matches found for the provided ingredients.");
        }

        // Return the list of cocktails if exact matches are found
        return ResponseEntity.ok(cocktails);
    }




    @GetMapping("/ingredients/partial")
    public ResponseEntity<Object> searchWithPartialIngredients(
            @RequestParam List<String> ingredients,
            @RequestParam(required = false) List<String> spirit_types) {

        // Validate the ingredients parameter
        if (ingredients == null || ingredients.isEmpty()) {
            return ResponseEntity.badRequest().body("No ingredients provided.");
        }

        // Normalize ingredients: remove spaces and convert to lowercase
        List<String> normalizedIngredients = ingredients.stream()
                .map(ingredient -> ingredient.toLowerCase().replace(" ", ""))
                .collect(Collectors.toList());

        // Search for partial matches (at least 2 ingredients)
        List<CocktailDTO> partialMatches = cocktailService.searchWithPartialIngredients(normalizedIngredients, spirit_types);

        if (partialMatches.isEmpty()) {
            return ResponseEntity.ok("No partial matches found for the provided ingredients.");
        }

        // Return the list of partial matches if found
        return ResponseEntity.ok(partialMatches);
    }


}
