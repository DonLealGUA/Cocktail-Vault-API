package com.example.cocktailvaultapi.Controller;

import com.example.cocktailvaultapi.DTO.CocktailDTO;
import com.example.cocktailvaultapi.DTO.CocktailIngredientDTO;
import com.example.cocktailvaultapi.Model.Cocktail;
import com.example.cocktailvaultapi.Service.CocktailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cocktails")
public class CocktailController {

    private final CocktailService cocktailService;

    public CocktailController(CocktailService cocktailService) {
        this.cocktailService = cocktailService;
    }

    @GetMapping
    public List<CocktailDTO> getAllCocktails() {
        List<Cocktail> cocktails = cocktailService.findAllCocktails(); // Ensure this fetches the cocktails
        return cocktails.stream()
                .map(this::convertToDTO) // Convert each cocktail to DTO
                .collect(Collectors.toList());
    }

    @GetMapping("/{name}")
    public ResponseEntity<CocktailDTO> getCocktailByName(@PathVariable("name") String name) {
        Optional<Cocktail> cocktail = cocktailService.findByName(name);
        return cocktail.map(this::convertToDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    private CocktailDTO convertToDTO(Cocktail cocktail) {
        CocktailDTO dto = new CocktailDTO();
        dto.setId(cocktail.getId());
        dto.setName(cocktail.getName());
        dto.setInstructions(cocktail.getInstructions());
        dto.setImageUrl(cocktail.getImageUrl());

        // Set glass type name
        if (cocktail.getGlassType() != null) {
            dto.setGlassType(cocktail.getGlassType().getName());
        }

        // Set spirit type name
        if (cocktail.getSpiritType() != null) {
            dto.setSpiritType(cocktail.getSpiritType().getName());
        }

        // Set ingredients list with names and quantities
        List<CocktailIngredientDTO> ingredientDTOs = cocktail.getCocktailIngredients().stream()
                .map(ci -> {
                    CocktailIngredientDTO cocktailIngredientDTO = new CocktailIngredientDTO();

                    // Set spirit type name if it exists
                    if (ci.getSpiritType() != null) {
                        cocktailIngredientDTO.setSpiritTypeName(ci.getSpiritType().getName());
                    }

                    // Only set ingredient name if spirit type is null
                    if (ci.getSpiritType() == null) {
                        if (ci.getIngredient() != null && ci.getIngredient().getName() != null) {
                            cocktailIngredientDTO.setIngredientName(ci.getIngredient().getName());
                        }
                    }

                    cocktailIngredientDTO.setQuantity(ci.getQuantity());

                    // Ensure ingredientName is null if it wasn't set
                    if (cocktailIngredientDTO.getIngredientName() == null && cocktailIngredientDTO.getSpiritTypeName() != null) {
                        cocktailIngredientDTO.setIngredientName(null); // Explicitly set to null if not present
                    }

                    return cocktailIngredientDTO;
                })
                .collect(Collectors.toList());

        dto.setIngredients(ingredientDTOs);

        return dto;
    }





}
