package com.example.cocktailvaultapi.Service.Implement;

import com.example.cocktailvaultapi.DTO.CocktailDTO;
import com.example.cocktailvaultapi.DTO.CocktailIngredientDTO;
import com.example.cocktailvaultapi.Model.Cocktail;
import com.example.cocktailvaultapi.Repository.CocktailRepository;
import com.example.cocktailvaultapi.Service.CocktailService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CocktailServiceImpl implements CocktailService {

    private final CocktailRepository cocktailRepository;

    public CocktailServiceImpl(CocktailRepository cocktailRepository) {
        this.cocktailRepository = cocktailRepository;
    }

    @Override
    public List<CocktailDTO> findAllCocktails() {
        List<Cocktail> cocktails = cocktailRepository.findAll();
        return cocktails.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<CocktailDTO> findByName(String name) {
        Optional<Cocktail> cocktail = cocktailRepository.findByName(name);
        if (cocktail.isPresent()) {
            CocktailDTO cocktailDTO = convertToDTO(cocktail.get());
            return ResponseEntity.ok(cocktailDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
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

        // Set spirit types list
        List<String> spiritTypes = cocktail.getCocktailIngredients().stream()
                .filter(ci -> ci.getSpiritType() != null)
                .map(ci -> ci.getSpiritType().getName())
                .distinct() // Ensure no duplicates
                .collect(Collectors.toList());

        dto.setSpiritTypes(spiritTypes); // Set the list of spirit types in DTO

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

        // Set the createdBy field
        dto.setCreatedBy(cocktail.getCreatedBy());

        return dto;
    }
}
