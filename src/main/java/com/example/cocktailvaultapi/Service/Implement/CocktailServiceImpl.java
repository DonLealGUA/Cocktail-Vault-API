package com.example.cocktailvaultapi.Service.Implement;

import com.example.cocktailvaultapi.DTO.CocktailDTO;
import com.example.cocktailvaultapi.DTO.CocktailIngredientDTO;
import com.example.cocktailvaultapi.DTO.PaginatedResponseDTO;
import com.example.cocktailvaultapi.Model.Cocktail;
import com.example.cocktailvaultapi.Repository.CocktailRepository;
import com.example.cocktailvaultapi.Service.CocktailService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CocktailServiceImpl implements CocktailService {

    private final CocktailRepository cocktailRepository;

    public CocktailServiceImpl(CocktailRepository cocktailRepository) {
        this.cocktailRepository = cocktailRepository;
    }


    /**
     * Retrieves all cocktail recipes without pagination.
     *
     * @return A list of CocktailDTO objects representing all cocktails.
     */
    @Override
    public List<CocktailDTO> searchAllCocktailsRecipes() {
        List<Cocktail> cocktails = cocktailRepository.findAll();
        return cocktails.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public String getCocktailCount() {
        return cocktailRepository.countTotalCocktails();
    }


    /**
     * Retrieves a paginated list of all cocktail recipes.
     *
     * @param page The page number to fetch (starting from 0).
     * @param size The number of results per page.
     * @return A PaginatedResponseDTO containing the CocktailDTO objects for the requested page.
     */
    @Override
    public PaginatedResponseDTO<CocktailDTO> searchAllCocktailsRecipesPageLimit(int page, int size) {
        return getPaginatedResponse(cocktailRepository.findAll(PageRequest.of(page, size)), page, size);
    }


    /**
     * Retrieves a paginated list of the latest cocktail recipes.
     * The cocktails are sorted by their creation date in descending order.
     *
     * @param page The page number to fetch (starting from 0).
     * @param size The number of results per page.
     * @return A PaginatedResponseDTO containing the latest CocktailDTO objects for the requested page.
     */
    @Override
    public PaginatedResponseDTO<CocktailDTO> listLatestCocktails(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Cocktail> paginatedCocktails = cocktailRepository.findLatestCocktails(pageable);
        return getPaginatedResponse(paginatedCocktails, page, size);
    }
    /**
     * Retrieves a random cocktail recipe.
     *
     * @return A ResponseEntity containing a random CocktailDTO. If no cocktail is found, returns a 404 response.
     */
    @Override
    public ResponseEntity<CocktailDTO> getRandomCocktail() {
        Optional<Cocktail> randomCocktailOptional = cocktailRepository.getRandomCocktail();
        return randomCocktailOptional.map(cocktail -> ResponseEntity.ok(convertToDTO(cocktail)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public PaginatedResponseDTO<CocktailDTO> getMultipleRandCocktails(String amount, int page, int size) {
        int total = Integer.parseInt(amount);

        Pageable pageable = PageRequest.of(page, size);

        Page<Cocktail> paginatedCocktails = cocktailRepository.getMultiRandCocktails(pageable);

        pageable = PageRequest.of(page, total);
        paginatedCocktails = cocktailRepository.getMultiRandCocktails(pageable);
        return getPaginatedResponse(paginatedCocktails, page, size);
    }



    /**
     * Retrieves a paginated list of cocktails based on a name search.
     * The search is case-insensitive and ignores spaces in the name.
     *
     * @param name The name of the cocktail to search for, case-insensitive and space-normalized.
     * @param page The page number to fetch (starting from 0).
     * @param size The number of results per page.
     * @return A PaginatedResponseDTO containing the CocktailDTO objects that match the specified name.
     */
    @Override
    public PaginatedResponseDTO<CocktailDTO> searchByNameIgnoreCase(String name, int page, int size) {
        String processedName = name.replace(" ", "").toLowerCase();
        Pageable pageable = PageRequest.of(page, size);
        Page<Cocktail> paginatedCocktails = cocktailRepository.findByNameIgnoreCaseAndSpaceInsensitive(processedName, pageable);
        return getPaginatedResponse(paginatedCocktails, page, size);
    }

    /**
     * Retrieves a paginated list of cocktails that start with the specified letter.
     * The search is case-insensitive, and pagination is applied based on the given parameters.
     *
     * @param letter The starting letter of the cocktail name.
     * @param page The page number to fetch (starting from 0).
     * @param size The number of results per page.
     * @return A PaginatedResponseDTO containing the CocktailDTO objects for the requested page.
     */
    @Override
    public PaginatedResponseDTO<CocktailDTO> listCocktailsByFirstLetter(char letter, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Cocktail> paginatedCocktails = cocktailRepository.findByNameStartingWithIgnoreCase(String.valueOf(letter), pageable);
        return getPaginatedResponse(paginatedCocktails, page, size);
    }

    /**
     * Retrieves a paginated list of cocktails based on the spirit brand.
     * The search is case-insensitive and space-insensitive.
     *
     * @param brand The brand of the spirit to search for.
     * @param page The page number to fetch (starting from 0).
     * @param size The number of results per page.
     * @return A PaginatedResponseDTO containing the CocktailDTO objects that match the specified spirit brand.
     */
    @Override
    public PaginatedResponseDTO<CocktailDTO> searchBySpiritBrand(String brand, int page, int size) {
        String brandWithoutSpaces = brand.replaceAll("\\s+", "");
        Pageable pageable = PageRequest.of(page, size);
        Page<Cocktail> paginatedCocktails = cocktailRepository.findBySpiritBrandIgnoreCase(brandWithoutSpaces, pageable);
        return getPaginatedResponse(paginatedCocktails, page, size);
    }

    /**
     * Retrieves a paginated list of cocktails based on the spirit type.
     * The search is case-insensitive and spaces are ignored.
     *
     * @param spiritType The type of spirit to search for.
     * @param page The page number to fetch (starting from 0).
     * @param size The number of results per page.
     * @return A PaginatedResponseDTO containing the CocktailDTO objects for the specified spirit type.
     */
    @Override
    public PaginatedResponseDTO<CocktailDTO> searchBySpiritType(String spiritType, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Cocktail> paginatedCocktails = cocktailRepository.findBySpiritTypeIgnoreCaseAndSpaces(spiritType, pageable);
        return getPaginatedResponse(paginatedCocktails, page, size);
    }


    /**
     * Filters cocktails by the type of glass they are served in.
     * The search is case-insensitive and space-insensitive.
     *
     * @param glassType The type of glass to filter cocktails by.
     * @param page The page number to fetch (starting from 0).
     * @param size The number of results per page.
     * @return A PaginatedResponseDTO containing the CocktailDTO objects that match the specified glass type.
     */
    @Override
    public PaginatedResponseDTO<CocktailDTO> filterByGlassType(String glassType, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Cocktail> paginatedCocktails = cocktailRepository.findByGlassTypeIgnoreCaseAndSpaces(glassType, pageable);
        return getPaginatedResponse(paginatedCocktails, page, size);
    }


    /**
     * Retrieves a paginated list of cocktails based on a specific ingredient search.
     * The search is case-insensitive and ignores spaces in the ingredient name.
     *
     * @param ingredient The ingredient to search for in the cocktail recipes, case-insensitive and space-normalized.
     * @param page The page number to fetch (starting from 0).
     * @param size The number of results per page.
     * @return A PaginatedResponseDTO containing the CocktailDTO objects that match the specified ingredient.
     */
    @Override
    public PaginatedResponseDTO<CocktailDTO> searchBySpecificIngredient(String ingredient, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Cocktail> paginatedCocktails = cocktailRepository.findCocktailsByIngredientNameIgnoreCaseAndSpaces(ingredient, pageable);

        return getPaginatedResponse(paginatedCocktails, page, size);
    }

    /**
     * Retrieves a paginated list of cocktails based on an exact match search for ingredients and spirits.
     * The search is case-insensitive and space-normalized for both ingredients and spirits.
     *
     * @param ingredients The list of ingredients to search for, case-insensitive and space-normalized.
     * @param spirits The list of spirits to filter cocktails by, case-insensitive and space-normalized.
     * @param page The page number to fetch (starting from 0).
     * @param size The number of results per page.
     * @return A PaginatedResponseDTO containing the CocktailDTO objects that match the specified ingredients and spirits.
     */
    @Override
    public PaginatedResponseDTO<CocktailDTO> searchWithExactIngredients(List<String> ingredients, List<String> spirits, int page, int size) {
        ingredients = transformList(ingredients);
        spirits = prepareList(spirits);

        Pageable pageable = PageRequest.of(page, size);

        Page<Cocktail> paginatedCocktails;
        if (!spirits.isEmpty()) {
            String ingredientsString = convertListToStringWithQuotes(ingredients);
            String spiritsString = convertListToStringWithQuotes(spirits);
            paginatedCocktails = cocktailRepository.findByExactIngredientsAndSpirits(ingredientsString, spiritsString, pageable);
        } else {
            paginatedCocktails = cocktailRepository.findCocktailsWithExactIngredients(ingredients, pageable);
        }

        return getPaginatedResponse(paginatedCocktails, page, size);
    }

    /**
     * Retrieves a paginated list of cocktails based on a partial ingredient match search.
     * The search is case-insensitive and ignores spaces in the ingredients list.
     *
     * @param ingredients The list of ingredients to search for (at least 2 ingredients), case-insensitive and space-normalized.
     * @param spirits The list of spirits to filter cocktails by, case-insensitive and space-normalized.
     * @param page The page number to fetch (starting from 0).
     * @param size The number of results per page.
     * @return A PaginatedResponseDTO containing the CocktailDTO objects that match the partial ingredients and optional spirits.
     */
    @Override
    public PaginatedResponseDTO<CocktailDTO> searchWithPartialIngredients(List<String> ingredients, List<String> spirits, int page, int size) {
        ingredients = transformList(ingredients);
        spirits = prepareList(spirits);

        String ingredientsString = convertListToStringWithQuotes(ingredients);
        String spiritsString = convertListToStringWithQuotes(spirits);

        Pageable pageable = PageRequest.of(page, size);

        Page<Cocktail> paginatedCocktails;
        if (!spirits.isEmpty()) {
            paginatedCocktails = cocktailRepository.findByPartialIngredientsAndSpirits(ingredientsString, spiritsString, pageable);
        } else {
            paginatedCocktails = cocktailRepository.findByPartialIngredients(ingredientsString, pageable);
        }

        return getPaginatedResponse(paginatedCocktails, page, size);
    }



    /**
     * Prepares a list of strings by transforming them to lowercase and removing spaces.
     * If the list is null, it returns an empty list.
     *
     * @param list The list of strings to transform.
     * @return A transformed list of strings, with spaces removed and all characters in lowercase.
     */
    private List<String> prepareList(List<String> list) {
        return list == null ? new ArrayList<>() : transformList(list);
    }

    /**
     * Returns a PaginatedResponseDTO containing the CocktailDTOs based on the paginated Cocktail data.
     *
     * @param paginatedCocktails The paginated Cocktail data.
     * @param page The current page number.
     * @param size The number of results per page.
     * @return A PaginatedResponseDTO containing the CocktailDTOs and pagination information.
     */
    private PaginatedResponseDTO<CocktailDTO> getPaginatedResponse(Page<Cocktail> paginatedCocktails, int page, int size) {
        List<CocktailDTO> cocktails = paginatedCocktails.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        PaginatedResponseDTO<CocktailDTO> response = new PaginatedResponseDTO<>();
        response.setData(cocktails);
        response.setTotalItems(paginatedCocktails.getTotalElements());
        response.setTotalPages(paginatedCocktails.getTotalPages());
        response.setCurrentPage(page);
        response.setPageSize(size);

        return response;
    }


    /**
     * Transforms a list of strings by trimming, converting them to lowercase, and removing spaces.
     *
     * @param list The input list of strings to transform.
     * @return A transformed list of strings.
     */
    private List<String> transformList(List<String> list) {
        return list.stream()
                .map(s -> s.trim().toLowerCase().replaceAll("\\s+", ""))
                .collect(Collectors.toList());
    }


    /**
     * Converts a list of strings to a single comma-separated string.
     *
     * @param items The list of items to convert to a string.
     * @return A single string containing all items joined by commas.
     */
    public String convertListToStringWithQuotes(List<String> items) {
        return String.join(",", items);
    }


    /**
     * Converts a Cocktail entity to a CocktailDTO.
     *
     * @param cocktail The Cocktail entity to convert.
     * @return A CocktailDTO containing the converted data.
     */
    private CocktailDTO convertToDTO(Cocktail cocktail) {
        CocktailDTO dto = new CocktailDTO();

        dto.setId(cocktail.getCocktailId());
        dto.setName(cocktail.getName());
        dto.setInstructions(cocktail.getInstructions());
        dto.setImageUrl(cocktail.getImageUrl());
        dto.setIceForm(cocktail.getIceForm());

        if (cocktail.getGlassType() != null) {
            dto.setGlassType(cocktail.getGlassType().getName());
        }

        List<String> spiritTypes = cocktail.getCocktailIngredients().stream()
                .filter(ci -> ci.getSpiritType() != null)
                .map(ci -> ci.getSpiritType().getName())
                .distinct()
                .collect(Collectors.toList());
        dto.setSpiritTypes(spiritTypes);

        List<CocktailIngredientDTO> ingredientDTOs = cocktail.getCocktailIngredients().stream()
                .map(ci -> {
                    CocktailIngredientDTO cocktailIngredientDTO = new CocktailIngredientDTO();

                    if (ci.getSpiritType() != null) {
                        cocktailIngredientDTO.setSpiritTypeName(ci.getSpiritType().getName());
                    }

                    if (ci.getSpiritType() == null && ci.getIngredient() != null) {
                        cocktailIngredientDTO.setIngredientName(ci.getIngredient().getName());
                    }

                    cocktailIngredientDTO.setQuantity(ci.getQuantity());

                    if (cocktailIngredientDTO.getIngredientName() == null && cocktailIngredientDTO.getSpiritTypeName() != null) {
                        cocktailIngredientDTO.setIngredientName(null);
                    }

                    return cocktailIngredientDTO;
                })
                .collect(Collectors.toList());
        dto.setIngredients(ingredientDTOs);
        
        dto.setCreatedBy(cocktail.getCreatedBy());
        dto.setCreatedByLink(cocktail.getCreatedByLink());
        dto.setCreatedDate(cocktail.getCreatedDate());
        dto.setSpiritBrand(cocktail.getSpiritBrand());

        return dto;
    }

}
