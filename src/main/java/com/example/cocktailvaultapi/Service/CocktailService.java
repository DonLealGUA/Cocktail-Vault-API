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

    /**
     * Retrieves all available cocktail recipes without any specific search filters.
     * This is a general method for fetching all cocktails.
     *
     * @return A list of CocktailDTO representing all cocktail recipes.
     */
    List<CocktailDTO> searchAllCocktailsRecipes();

    String getCocktailCount();

    /**
     * Retrieves a paginated list of cocktail recipes with a specified page number and page size.
     * Useful for displaying large numbers of cocktails in a paginated format.
     *
     * @param page The page number to retrieve.
     * @param size The number of results per page.
     * @return A PaginatedResponseDTO containing a list of CocktailDTO objects for the requested page.
     */
    PaginatedResponseDTO<CocktailDTO> searchAllCocktailsRecipesPageLimit(int page, int size);

    /**
     * Retrieves a random cocktail recipe.
     * Useful for recommending a random cocktail to users.
     *
     * @return A ResponseEntity containing a random CocktailDTO.
     */
    ResponseEntity<CocktailDTO> getRandomCocktail();

    /**
     * Retrieves the most recent cocktail recipes, based on the number of cocktails to return.
     * This method is useful for showcasing the latest additions to the collection.
     *
     * @param page The page number to retrieve.
     * @param size The number of results per page.
     * @return A list of CocktailDTO containing the most recently added cocktails.
     */
    PaginatedResponseDTO<CocktailDTO> listLatestCocktails(int page, int size) ;

    /**
     * Searches for cocktail recipes based on the cocktail name, ignoring case sensitivity.
     * This is helpful for users searching by name without worrying about capitalization.
     *
     * @param name The name of the cocktail to search for.
     * @param page The page number to retrieve.
     * @param size The number of results per page.
     * @return A ResponseEntity containing the CocktailDTO if found, or an appropriate error message if not found.
     */
    PaginatedResponseDTO<CocktailDTO> searchByNameIgnoreCase(String name,int page, int size);

    /**
     * Retrieves a list of cocktails that start with a specific letter.
     * Useful for browsing cocktails by their initial letter.
     *
     * @param letter The first letter of the cocktail name to filter by.
     * @param size The number of results per page.
     * @param page The page number to retrieve.
     * @return A list of CocktailDTO representing cocktails starting with the specified letter.
     */
    PaginatedResponseDTO<CocktailDTO>  listCocktailsByFirstLetter(char letter,int page, int size);

    /**
     * Searches for cocktail recipes based on a specific spirit brand.
     * For example, this method allows filtering by well-known brands like Absolut.
     *
     * @param brand The brand of the spirit to filter cocktails by.
     * @param page The page number to retrieve.
     * @param size The number of results per page.
     * @return A list of CocktailDTO representing cocktails containing the specified brand of spirit.
     */
    PaginatedResponseDTO<CocktailDTO>  searchBySpiritBrand(String brand,int page, int size);

    /**
     * Searches for cocktail recipes based on the type of spirit (e.g., Vodka, Whiskey).
     * This is useful for users looking for cocktails that use a specific type of spirit.
     *
     * @param spiritType The type of spirit (e.g., Vodka, Whiskey) to filter cocktails by.
     * @param size The number of results per page.
     * @param page The page number to retrieve.
     * @return A list of CocktailDTO representing cocktails that use the specified type of spirit.
     */
    PaginatedResponseDTO<CocktailDTO> searchBySpiritType(String spiritType,int page, int size);

    /**
     * Filters cocktails based on the type of glass they are traditionally served in.
     * This method is useful for users who want to select cocktails based on glassware.
     *
     * @param glassType The type of glass (e.g., Martini Glass, Highball Glass) to filter by.
     * @param size The number of results per page.
     * @param page The page number to retrieve.
     * @return A list of CocktailDTO representing cocktails typically served in the specified glass type.
     */
    PaginatedResponseDTO<CocktailDTO> filterByGlassType(String glassType,int page, int size);

    /**
     * Searches for cocktail recipes that include a specific ingredient.
     * Useful for users who want cocktails based on an ingredient they have available.
     *
     * @param ingredient The ingredient to search for in the cocktail recipes.
     * @param size The number of results per page.
     * @param page The page number to retrieve.
     * @return A list of CocktailDTO representing cocktails containing the specified ingredient.
     */
    PaginatedResponseDTO<CocktailDTO> searchBySpecificIngredient(String ingredient,int page, int size);

    /**
     * Searches for cocktails that contain only the specified list of ingredients and spirits.
     * This is useful when users want cocktails with a precise set of ingredients.
     *
     * @param ingredients A list of ingredients that must be included in the cocktails.
     * @param spirits A list of spirits that must be included in the cocktails.
     * @param size The number of results per page.
     * @param page The page number to retrieve.
     * @return A list of CocktailDTO representing cocktails that match the exact ingredients and spirits.
     */
    PaginatedResponseDTO<CocktailDTO> searchWithExactIngredients(List<String> ingredients, List<String> spirits,int page, int size);

    /**
     * Searches for cocktails that contain any of the specified ingredients, with an optional list of spirits.
     * This method is useful for users who want cocktails based on partial ingredients and spirit preferences.
     *
     * @param normalizedIngredients A list of ingredients to search for.
     * @param spirits A list of spirits to filter by (optional).
     * @param size The number of results per page.
     * @param page The page number to retrieve.
     * @return A list of CocktailDTO representing cocktails that match the specified ingredients and spirits.
     */
    PaginatedResponseDTO<CocktailDTO> searchWithPartialIngredients(List<String> normalizedIngredients, List<String> spirits,int page, int size);


    PaginatedResponseDTO<CocktailDTO> getMultipleRandCocktails(String amount, int page, int size);
}
