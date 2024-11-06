package com.example.cocktailvaultapi.Controller;

import com.example.cocktailvaultapi.DTO.CocktailDTO;
import com.example.cocktailvaultapi.DTO.PaginatedResponseDTO;
import com.example.cocktailvaultapi.Model.Cocktail;
import com.example.cocktailvaultapi.Service.CocktailService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cocktails")
public class CocktailController {

    private final CocktailService cocktailService; // Use the interface instead of the implementation

    public CocktailController(CocktailService cocktailService) {
        this.cocktailService = cocktailService;
    }

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



    @GetMapping("/{name}")
    public ResponseEntity<CocktailDTO> getCocktailByName(@PathVariable("name") String name) {
        return cocktailService.searchByNameIgnoreCase(name);
    }

    @GetMapping("/by-letter/{letter}")
    public List<CocktailDTO> listCocktailsByFirstLetter(@PathVariable char letter) {
        return cocktailService.listCocktailsByFirstLetter(letter);
    }
}
