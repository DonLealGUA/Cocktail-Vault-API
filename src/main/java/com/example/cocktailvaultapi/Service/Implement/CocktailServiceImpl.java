package com.example.cocktailvaultapi.Service.Implement;

import com.example.cocktailvaultapi.DTO.CocktailDTO;
import com.example.cocktailvaultapi.DTO.CocktailIngredientDTO;
import com.example.cocktailvaultapi.DTO.PaginatedResponseDTO;
import com.example.cocktailvaultapi.Model.Cocktail;
import com.example.cocktailvaultapi.Repository.CocktailRepository;
import com.example.cocktailvaultapi.Repository.SpiritRepository;
import com.example.cocktailvaultapi.Repository.ingredientRepository;
import com.example.cocktailvaultapi.Service.CocktailService;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CocktailServiceImpl implements CocktailService {

    private final CocktailRepository cocktailRepository;
    private final SpiritRepository spiritRepository;
    private final ingredientRepository ingredientRepository;

    public CocktailServiceImpl(CocktailRepository cocktailRepository, SpiritRepository spiritRepository, com.example.cocktailvaultapi.Repository.ingredientRepository ingredientRepository) {
        this.cocktailRepository = cocktailRepository;
        this.spiritRepository = spiritRepository;
        this.ingredientRepository = ingredientRepository;
    }

    //TODO
    // Make every list a PaginatedResponseDTO

    @Override
    public List<CocktailDTO> searchAllCocktailsRecipes() {
        List<Cocktail> cocktails = cocktailRepository.findAll();
        return cocktails.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PaginatedResponseDTO<CocktailDTO> searchAllCocktailsRecipesPageLimit(int page, int size) {
        Page<Cocktail> paginatedCocktails = cocktailRepository.findAll(PageRequest.of(page, size));

        List<CocktailDTO> cocktails = paginatedCocktails.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        // Create and return PaginatedResponseDTO with pagination data
        PaginatedResponseDTO<CocktailDTO> response = new PaginatedResponseDTO<>();
        response.setData(cocktails);
        response.setTotalItems(paginatedCocktails.getTotalElements());
        response.setTotalPages(paginatedCocktails.getTotalPages());
        response.setCurrentPage(page);
        response.setPageSize(size);

        return response;
    }

    @Override
    public List<CocktailDTO> listLatestCocktails(int count) {
        Pageable pageable = PageRequest.of(0, count);
        return cocktailRepository.findLatestCocktails(pageable).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<CocktailDTO> getRandomCocktail() {
        // Fetch a random cocktail directly from the database (using a native query or custom repository method)
        Optional<Cocktail> randomCocktailOptional = cocktailRepository.getRandomCocktail();

        if (randomCocktailOptional.isEmpty()) {
            return ResponseEntity.notFound().build(); // Return 404 if no cocktail found
        }

        // Convert the cocktail to DTO
        CocktailDTO cocktailDTO = convertToDTO(randomCocktailOptional.get());

        return ResponseEntity.ok(cocktailDTO); // Return 200 OK with the cocktail data
    }


    @Override
    public ResponseEntity<CocktailDTO> searchByNameIgnoreCase(String name) {
        Optional<Cocktail> cocktail = cocktailRepository.findByNameIgnoreCase(name);
        if (cocktail.isPresent()) {
            CocktailDTO cocktailDTO = convertToDTO(cocktail.get());
            return ResponseEntity.ok(cocktailDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public List<CocktailDTO> listCocktailsByFirstLetter(char letter) {
        return cocktailRepository.findByNameStartingWithIgnoreCase(String.valueOf(letter)).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    @Override
    public List<CocktailDTO> searchBySpiritBrand(String brand) {
        return cocktailRepository.findBySpiritBrandIgnoreCase(brand).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CocktailDTO> searchBySpiritType(String spiritType) {
        return cocktailRepository.findBySpiritTypeIgnoreCaseAndSpaces(spiritType).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CocktailDTO> filterByGlassType(String glassType) {
        return cocktailRepository.findByGlassTypeIgnoreCaseAndSpaces(glassType).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CocktailDTO> searchBySpecificIngredient(String ingredient) {
        return cocktailRepository.findCocktailsByIngredientNameIgnoreCaseAndSpaces(ingredient).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    // Method to transform the list: lowercase and remove spaces
    private List<String> transformList(List<String> list) {
        return list.stream()
                .map(s -> s.trim().toLowerCase().replaceAll("\\s+", ""))  // Trim, convert to lowercase, and remove spaces
                .collect(Collectors.toList());
    }

    public String convertListToStringWithQuotes(List<String> items) {
        String joinedString = String.join(",", items);
        return joinedString;  // Return as a plain string without single quotes
    }


    // Service Layer: Search for exact ingredient matches
    @Override
    public List<CocktailDTO> searchWithExactIngredients(List<String> ingredients, List<String> spiritTypes) {
        // Transform ingredients to lowercase and remove spaces
        ingredients = transformList(ingredients);  // Valid ingredients without spaces and in lowercase

        // Handle spiritTypes
        if (spiritTypes != null) {
            spiritTypes = transformList(spiritTypes);  // All spirit types without spaces and in lowercase
        } else {
            spiritTypes = new ArrayList<>(); // Default to an empty list if spiritTypes is null
        }

        // Log the transformed lists for debugging purposes
        System.out.println("Spirit Types: " + spiritTypes);  // This was previously logging ingredients, updated to reflect spiritTypes
        System.out.println("Ingredients: " + ingredients);    // Logging ingredients

        // Fetch cocktails that match the exact ingredients
        List<Cocktail> cocktails;
        if (!spiritTypes.isEmpty()) {
            // If spiritTypes are provided, find cocktails with matching spirit types and ingredients
            String ingredientsString = convertListToStringWithQuotes(ingredients);  // No quotes around the ingredients string
            String spiritTypesString  = convertListToStringWithQuotes(spiritTypes);  // No quotes around the spirit types string

            System.out.println("Spirit Types STRING: " + spiritTypesString);  // Logging spirit types
            System.out.println("Ingredients STRING: " + ingredientsString);    // Logging ingredients

            cocktails = cocktailRepository.findByExactIngredientsAndSpirits(ingredientsString, spiritTypesString);
        } else {
            // Otherwise, just find cocktails with matching ingredients only
            cocktails = cocktailRepository.findCocktailsWithExactIngredients(ingredients);
        }

        // Convert cocktails to DTOs and return
        return cocktails.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }






    // Service Layer: Search for partial ingredient matches (at least 2 ingredients)
    @Override
    public List<CocktailDTO> searchWithPartialIngredients(List<String> ingredients, List<Integer> spiritTypeIds) {
        // Fetch cocktails from the repository
        List<Cocktail> cocktails;
        if (spiritTypeIds != null && !spiritTypeIds.isEmpty()) {
            // If spiritTypeIds are provided, search for partial matches with spirits
            cocktails = cocktailRepository.findByPartialIngredientsAndSpirits(ingredients, spiritTypeIds);
        } else {
            // If no spiritTypeIds are provided, only search by ingredients
            cocktails = cocktailRepository.findByPartialIngredients(ingredients);
        }

        // Convert the Cocktail entities to CocktailDTOs
        return cocktails.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    private List<String> fetchSpiritNames() {
        // Fetch all spirit names from the database and return them as a list of strings
        return spiritRepository.findAll().stream()
                .map(spirit -> spirit.getName().toLowerCase().replace(" ", ""))
                .collect(Collectors.toList());
    }


    private CocktailDTO convertToDTO(Cocktail cocktail) {
        CocktailDTO dto = new CocktailDTO();
        dto.setId(cocktail.getCocktailId());
        dto.setName(cocktail.getName());
        dto.setInstructions(cocktail.getInstructions());
        dto.setImageUrl(cocktail.getImageUrl());
        dto.setIceForm(cocktail.getIceForm());

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
        dto.setCreatedByLink(cocktail.getCreatedByLink());
        dto.setCreatedDate(cocktail.getCreatedDate());
        dto.setSpiritBrand(cocktail.getSpiritBrand());
        return dto;
    }
}
