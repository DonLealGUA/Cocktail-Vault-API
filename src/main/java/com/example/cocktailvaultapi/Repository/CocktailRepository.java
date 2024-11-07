package com.example.cocktailvaultapi.Repository;

import com.example.cocktailvaultapi.Model.Cocktail;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CocktailRepository extends JpaRepository<Cocktail, Long> {

    // Custom query method
    Optional<Cocktail> findByNameIgnoreCase(String name); // Search for cocktails by name
    List<Cocktail> findByNameStartingWithIgnoreCase(String letter); // List all cocktails starting with a specific letter
    @Query(value = "SELECT * FROM cocktails ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    Optional<Cocktail> getRandomCocktail(); // Get a random cocktail
    @Query("SELECT c FROM Cocktail c ORDER BY c.createdDate DESC")
    List<Cocktail> findLatestCocktails(Pageable pageable);// List the latest cocktails added


    List<Cocktail> findBySpiritBrandIgnoreCase(String brand); // Get recipes by specific spirit brand (e.g., Absolut)
    @Query("SELECT c FROM Cocktail c JOIN c.cocktailSpirits cs JOIN cs.spiritType st WHERE LOWER(st.name) = LOWER(:spiritType)")
    List<Cocktail> findBySpiritTypeIgnoreCaseAndSpaces(@Param("spiritType") String spiritType); // Search cocktails by spirit type
    @Query("SELECT c FROM Cocktail c JOIN c.glassType g WHERE LOWER(REPLACE(g.name, ' ', '')) = LOWER(REPLACE(:glassType, ' ', ''))")
    List<Cocktail> findByGlassTypeIgnoreCaseAndSpaces(@Param("glassType") String glassType); // Filter cocktails by glass type




    //TODO
    // Fixa exakt match med och utan sprit
    //
    // Gör så att den hämtar halvfärdiga cocktails

    @Query("SELECT c FROM Cocktail c " + "JOIN c.cocktailIngredients ci " + "JOIN ci.ingredient i " + "WHERE LOWER(REPLACE(i.name, ' ', '')) = LOWER(REPLACE(:ingredientName, ' ', ''))")
    List<Cocktail> findCocktailsByIngredientNameIgnoreCaseAndSpaces(@Param("ingredientName") String ingredientName);


    // Repository Layer: Query for exact ingredient matches (no extras)
    /**
     * Finds cocktails that contain exactly the specified ingredients.
     *
     * @param ingredients      List of normalized ingredient names (lowercase, no spaces).
     * @param ingredientCount  The number of ingredients to match.
     * @return List of matching cocktails.
     */
    @Query("SELECT c FROM Cocktail c " +
            "JOIN c.cocktailIngredients ci " +
            "JOIN ci.ingredient i " +
            "WHERE LOWER(i.name) IN :ingredients " +
            "AND ci.spiritType IS NULL " +  // Exclude spirit ingredients
            "GROUP BY c.cocktailId, c.name " +
            "HAVING COUNT(DISTINCT i.name) = :ingredientCount " +  // Exact match of the distinct ingredients in the list
            "AND (SELECT COUNT(*) FROM CocktailIngredient ci2 " +
            "      JOIN ci2.ingredient i2 " +
            "WHERE ci2.cocktail = c " +  // Referencing the `cocktail` field in `CocktailIngredient`
            "AND ci2.spiritType IS NULL) = :ingredientCount")  // Ensure total non-spirit ingredient count matches
    List<Cocktail> findCocktailsWithExactIngredients(List<String> ingredients, int ingredientCount);






    // Repository Layer: Query for exact matches for both ingredients and spirits (no extras)






    // Repository Layer: Query for partial ingredient matches (at least 2 ingredients)
    @Query("SELECT c FROM Cocktail c " +
            "JOIN c.cocktailIngredients ci " +
            "JOIN ci.ingredient i " +
            "WHERE LOWER(REPLACE(i.name, ' ', '')) IN :ingredients " +
            "GROUP BY c.cocktailId HAVING COUNT(DISTINCT i.name) >= 2")
    List<Cocktail> findByPartialIngredients(@Param("ingredients") List<String> ingredients);

    // Repository Layer: Query for partial ingredient and spirit matches
    @Query("SELECT c FROM Cocktail c " +
            "JOIN c.cocktailIngredients ci " +
            "JOIN ci.ingredient i " +
            "JOIN c.cocktailSpirits cs " +
            "JOIN cs.spiritType s " +
            "WHERE LOWER(REPLACE(i.name, ' ', '')) IN :ingredients " +
            "AND (s.spiritTypeId IN :spirits OR :spirits IS NULL) " +
            "GROUP BY c.cocktailId HAVING COUNT(DISTINCT i.name) >= 2")
    List<Cocktail> findByPartialIngredientsAndSpirits(@Param("ingredients") List<String> ingredients, @Param("spirits") List<Integer> spirits);


// The cocktails need to contain exactly the ingredients you provide (but not necessarily all of them),




}
