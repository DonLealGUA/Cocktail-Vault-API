package com.example.cocktailvaultapi.Controller;

import com.example.cocktailvaultapi.DTO.CocktailDTO;
import com.example.cocktailvaultapi.DTO.PaginatedResponseDTO;
import com.example.cocktailvaultapi.Exception.CustomException;
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
     * Returns a response entity with the error message if no cocktails match the search or the input is invalid.
     *
     * @param ex The custom exception thrown.
     * @return A ResponseEntity containing the error message.
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<String> handleCustomException(CustomException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    /**
     * Default mapping: /api/cocktails
     * Returns a list of all cocktails.
     * If no cocktails are found, throws a CustomException.
     *
     * @return A list of all cocktails.
     * @throws CustomException if no cocktails are found.
     */
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping
    public List<CocktailDTO> getAllCocktails() {
        List<CocktailDTO> cocktails = cocktailService.searchAllCocktailsRecipes();
        if (cocktails.isEmpty()) {
            throw new CustomException("No cocktails found");
        }
        return cocktails;
    }

    /**
     * Returns the amount of Cocktails
     * Example: http://localhost:8080/api/cocktails/count
     *
     * @return A string of the total cocktails in the DB.
     */
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/count")
    public String getCocktailCount() {

        return cocktailService.getCocktailCount();
    }

    /**
     * Returns a paginated list of all cocktails.
     * If no results are found for the given page and size, throws a CustomException.
     * Example: http://localhost:8080/api/cocktails/page?page=0&size=10
     *
     * @param page The page number for pagination.
     * @param size The number of items per page.
     * @return A paginated list of cocktails.
     * @throws CustomException if no cocktails are found for the given page and size.
     */
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/page")
    public ResponseEntity<PaginatedResponseDTO<CocktailDTO>> getAllCocktailsPageLimit(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PaginatedResponseDTO<CocktailDTO> response = cocktailService.searchAllCocktailsRecipesPageLimit(page, size);

        if (response.getData().isEmpty()) {
            throw new CustomException("No cocktails found for the given page and size");
        }

        return ResponseEntity.ok(response);
    }


    /**
     * Returns a paginated list of the latest cocktails.
     * If no results are found for the given page and size, throws a CustomException.
     * Example: http://localhost:8080/api/cocktails/latest
     * Or: http://localhost:8080/api/cocktails/latest?page=0&size=10
     *
     * @param page The page number for pagination.
     * @param size The number of items per page.
     * @return A paginated list of the latest cocktails.
     * @throws CustomException if no latest cocktails are found.
     */
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/latest")
    public ResponseEntity<PaginatedResponseDTO<CocktailDTO>> listLatestCocktails(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PaginatedResponseDTO<CocktailDTO> response = cocktailService.listLatestCocktails(page, size);

        if (response.getData().isEmpty()) {
            throw new CustomException("No latest cocktails found");
        }

        return ResponseEntity.ok(response);
    }

    /**
     * Returns a random cocktail.
     * If no random cocktail is found, throws a CustomException.
     * Exmaple: http://localhost:8080/api/cocktails/random
     *
     * @return A random cocktail.
     * @throws CustomException if no random cocktail is found.
     */
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/random")
    public ResponseEntity<CocktailDTO> getRandomCocktail() {
        CocktailDTO cocktail = cocktailService.getRandomCocktail().getBody();
        if (cocktail == null) {
            throw new CustomException("No random cocktail found");
        }
        return ResponseEntity.ok(cocktail);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/multirandom/{amount}")
    public ResponseEntity<PaginatedResponseDTO<CocktailDTO>> getMultipleRandCocktails(
            @PathVariable("amount") String amount,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PaginatedResponseDTO<CocktailDTO> response = cocktailService.getMultipleRandCocktails(amount,page, size);

        if (response.getData().isEmpty()) {
            throw new CustomException("No cocktails found with the name: " + amount);
        }

        return ResponseEntity.ok(response);
    }


    /**
     * Returns a paginated list of cocktails by name.
     * If no cocktails are found with the given name, throws a CustomException.
     * Example: http://localhost:8080/api/cocktails/Mojito
     *
     * @param name The name of the cocktail.
     * @param page The page number for pagination.
     * @param size The number of items per page.
     * @return A paginated list of cocktails matching the name.
     * @throws CustomException if no cocktails are found with the given name.
     */
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/{name}")
    public ResponseEntity<PaginatedResponseDTO<CocktailDTO>> getCocktailByName(
            @PathVariable("name") String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PaginatedResponseDTO<CocktailDTO> response = cocktailService.searchByNameIgnoreCase(name, page, size);

        if (response.getData().isEmpty()) {
            throw new CustomException("No cocktails found with the name: " + name);
        }

        return ResponseEntity.ok(response);
    }


    /**
     * Returns a paginated list of cocktails by first letter.
     * If an invalid letter is provided or no cocktails are found, throws a CustomException.
     * Example: http://localhost:8080/api/cocktails/by-letter/M
     *
     * @param letter The first letter of the cocktail name.
     * @param page The page number for pagination.
     * @param size The number of items per page.
     * @return A paginated list of cocktails starting with the given letter.
     * @throws CustomException if the letter is invalid or no cocktails are found with the given letter.
     */
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/by-letter/{letter}")
    public ResponseEntity<PaginatedResponseDTO<CocktailDTO>> listCocktailsByFirstLetter(
            @PathVariable("letter") char letter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        if (Character.isDigit(letter)) {
            throw new CustomException("Invalid letter: " + letter + ". Must be a letter.");
        }

        PaginatedResponseDTO<CocktailDTO> response = cocktailService.listCocktailsByFirstLetter(letter, page, size);

        if (response.getData().isEmpty()) {
            throw new CustomException("No cocktails found starting with: " + letter);
        }

        return ResponseEntity.ok(response);
    }


    /**
     * Returns a paginated list of cocktails filtered by spirit brand.
     * If no cocktails are found for the given brand, throws a CustomException.
     * Example: http://localhost:8080/api/cocktails/filter/brand/Bacardi
     *
     * @param brand The spirit brand to filter by.
     * @param page The page number for pagination.
     * @param size The number of items per page.
     * @return A paginated list of cocktails filtered by spirit brand.
     * @throws CustomException if no cocktails are found for the given brand.
     */
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/filter/brand/{brand}")
    public ResponseEntity<PaginatedResponseDTO<CocktailDTO>> getCocktailsBySpiritBrand(
            @PathVariable("brand") String brand,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PaginatedResponseDTO<CocktailDTO> response = cocktailService.searchBySpiritBrand(brand, page, size);

        if (response.getData().isEmpty()) {
            throw new CustomException("No cocktails found with the brand: " + brand);
        }

        return ResponseEntity.ok(response);
    }

    /**
     * Returns a paginated list of cocktails filtered by spirit type.
     * If no cocktails are found for the given spirit type, throws a CustomException.
     * http://localhost:8080/api/cocktails/filter/spirit/Rum
     *
     * @param spiritType The spirit type to filter by.
     * @param page The page number for pagination.
     * @param size The number of items per page.
     * @return A paginated list of cocktails filtered by spirit type.
     * @throws CustomException if no cocktails are found for the given spirit type.
     */
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/filter/spirit/{spirit}")
    public ResponseEntity<PaginatedResponseDTO<CocktailDTO>> filterBySpiritType(
            @PathVariable("spirit") String spiritType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PaginatedResponseDTO<CocktailDTO> response = cocktailService.searchBySpiritType(spiritType, page, size);

        if (response.getData().isEmpty()) {
            throw new CustomException("No cocktails found with the spirit type: " + spiritType);
        }

        return ResponseEntity.ok(response);
    }

    /**
     * Returns a paginated list of cocktails filtered by glass type.
     * If no cocktails are found for the given glass type, throws a CustomException.
     * Example: http://localhost:8080/api/cocktails/filter/glass/Highball%20Glass
     *
     * @param glassType The glass type to filter by.
     * @param page The page number for pagination.
     * @param size The number of items per page.
     * @return A paginated list of cocktails filtered by glass type.
     * @throws CustomException if no cocktails are found for the given glass type.
     */
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/filter/glass/{glassType}")
    public ResponseEntity<PaginatedResponseDTO<CocktailDTO>> filterByGlassType(
            @PathVariable("glassType") String glassType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PaginatedResponseDTO<CocktailDTO> response = cocktailService.filterByGlassType(glassType, page, size);

        if (response.getData().isEmpty()) {
            throw new CustomException("No cocktails found with the glass type: " + glassType);
        }

        return ResponseEntity.ok(response);
    }

    /**
     * Returns a paginated list of cocktails containing a specific ingredient.
     * If no cocktails are found with the given ingredient, throws a CustomException.
     * Example: http://localhost:8080/api/cocktails/filter/ingredient/Lime
     *
     * @param ingredient The ingredient to filter by.
     * @param page The page number for pagination.
     * @param size The number of items per page.
     * @return A paginated list of cocktails containing the given ingredient.
     * @throws CustomException if no cocktails are found with the given ingredient.
     */
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/filter/ingredient/{ingredient}")
    public ResponseEntity<PaginatedResponseDTO<CocktailDTO>> searchBySpecificIngredient(
            @PathVariable("ingredient") String ingredient,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){

        PaginatedResponseDTO<CocktailDTO> response = cocktailService.searchBySpecificIngredient(ingredient,page,size);

        if (response.getData().isEmpty()) {
            throw new CustomException("No cocktails found with the ingredient: " + ingredient);
        }

        return ResponseEntity.ok(response);
    }

    /**
     * Searches for cocktails that contain the exact specified ingredients and optional spirit types.
     * Example 1: http://localhost:8080/api/cocktails/ingredients/exact?ingredients=Ice&ingredients=Coconut%20cream&ingredients=Pineapple%20juice&ingredients=Sugar&ingredients=Lime&ingredients=Soda%20water&ingredients=Mint%20leaves
     * Example 2: http://localhost:8080/api/cocktails/ingredients/exact?ingredients=Ice&ingredients=Coconut%20cream&ingredients=Pineapple%20juice&spirit_types=Vodka&spirit_types=Dry%20Vermouth
     *
     * @param ingredients The list of ingredients to search for, required. Example: ["Ice", "Coconut cream", "Pineapple juice"].
     * @param spirit_types The optional list of spirit types. Example: ["Vodka", "Dry Vermouth"].
     * @param page The page number for pagination (default is 0). Example: 0.
     * @param size The page size for pagination (default is 10). Example: 10.
     * @return A ResponseEntity containing a PaginatedResponseDTO with matching cocktails.
     * @throws CustomException if no cocktails are found or if the ingredients list is empty.
     */
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/ingredients/exact")
    public ResponseEntity<PaginatedResponseDTO<CocktailDTO>> searchWithExactIngredients(
            @RequestParam List<String> ingredients,
            @RequestParam(required = false) List<String> spirit_types,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        // Validate the ingredients parameter
        if (ingredients == null || ingredients.isEmpty()) {
            // Throw a CustomException if no ingredients are provided
            throw new CustomException("Ingredients list cannot be empty");
        }

        // If spirit_types is null, default it to an empty list
        if (spirit_types == null) {
            spirit_types = new ArrayList<>();
        }

        // Search for exact matches with pagination
        PaginatedResponseDTO<CocktailDTO> response = cocktailService.searchWithExactIngredients(ingredients, spirit_types, page, size);

        // Check if no results were found
        if (response.getData().isEmpty()) {
            // Throw a CustomException if no cocktails were found
            throw new CustomException("No cocktails found for the provided ingredients");
        }

        // Return the list of cocktails if matches are found
        return ResponseEntity.ok(response);
    }

    /**
     * Searches for cocktails that contain the partial specified ingredients and optional spirit types.
     * Example 1: http://localhost:8080/api/cocktails/ingredients/partial?&ingredients=Lime&ingredients=Mint%20Leaves
     *
     * @param ingredients The list of ingredients to search for, required. Example: ["Lime", "Mint Leaves"].
     * @param spirit_types The optional list of spirit types. Example: ["Rum"].
     * @param page The page number for pagination (default is 0). Example: 0.
     * @param size The page size for pagination (default is 10). Example: 10.
     * @return A ResponseEntity containing a PaginatedResponseDTO with matching cocktails.
     * @throws CustomException if no cocktails are found or if the ingredients list is empty.
     */
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/ingredients/partial")
    public ResponseEntity<PaginatedResponseDTO<CocktailDTO>> searchWithPartialIngredients(
            @RequestParam List<String> ingredients,
            @RequestParam(required = false) List<String> spirit_types,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        // Validate the ingredients parameter
        if (ingredients == null || ingredients.isEmpty()) {
            // Throw a CustomException if no ingredients are provided
            throw new CustomException("Ingredients list cannot be empty");
        }

        // If spirit_types is null, default it to an empty list
        if (spirit_types == null) {
            spirit_types = new ArrayList<>();
        }

        // Normalize ingredients: remove spaces and convert to lowercase
        List<String> normalizedIngredients = ingredients.stream()
                .map(ingredient -> ingredient.toLowerCase().replace(" ", "")) // Remove spaces and convert to lowercase
                .collect(Collectors.toList());

        // Search for partial matches with pagination
        PaginatedResponseDTO<CocktailDTO> response = cocktailService.searchWithPartialIngredients(
                normalizedIngredients, spirit_types, page, size);

        // Check if no results were found
        if (response.getData().isEmpty()) {
            // Throw a CustomException if no cocktails were found
            throw new CustomException("No cocktails found for the provided partial ingredients");
        }

        // Return the list of cocktails if matches are found
        return ResponseEntity.ok(response);
    }


}
