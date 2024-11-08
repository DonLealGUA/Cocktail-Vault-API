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
     * @param ingredients List of normalized ingredient names (lowercase, no spaces).
     * @return List of matching cocktails.
     */
    @Query("SELECT DISTINCT c FROM Cocktail c " +
            "JOIN c.cocktailIngredients ci " +
            "JOIN ci.ingredient i " +
            "WHERE REPLACE(LOWER(i.name), ' ', '') IN :ingredients " +
            "AND i.type NOT IN ('garnish') " +
            "AND ci.ingredient IS NOT NULL " +
            "GROUP BY c.cocktailId " +
            "HAVING COUNT(DISTINCT ci.ingredient) = " +
            "(SELECT COUNT(DISTINCT ci2.ingredient) " +
            "FROM CocktailIngredient ci2 " +
            "WHERE ci2.cocktail.cocktailId = c.cocktailId " +
            "AND ci2.ingredient IS NOT NULL)"
    )
    List<Cocktail> findCocktailsWithExactIngredients(List<String> ingredients);

















    // Repository Layer: Query for exact matches for both ingredients and spirits (no extras)
    @Query(value = """
WITH available_ingredients AS (
    SELECT ingredient_id
    FROM ingredients i
    JOIN LATERAL unnest(string_to_array(:ingredients, ',')) AS ingredient_name ON TRUE
    WHERE TRIM(LOWER(REPLACE(i.name, ' ', ''))) = LOWER(REPLACE(ingredient_name, ' ', ''))
    AND TRIM(LOWER(REPLACE(i.type, ' ', ''))) NOT IN (LOWER(REPLACE('garnish', ' ', '')))
),
available_spirits AS (
    SELECT spirit_type_id
    FROM spirit_types
    JOIN LATERAL unnest(string_to_array(:spiritTypes, ',')) AS spirit_name ON TRUE
    WHERE TRIM(LOWER(REPLACE(name, ' ', ''))) = LOWER(REPLACE(spirit_name, ' ', ''))
),
matching_cocktails AS (
    SELECT ci.cocktail_id
    FROM cocktail_ingredients ci
    JOIN available_ingredients ai ON ci.ingredient_id = ai.ingredient_id
    GROUP BY ci.cocktail_id
    HAVING COUNT(DISTINCT ci.ingredient_id) = (
        SELECT COUNT(DISTINCT ingredient_id)
        FROM cocktail_ingredients
        WHERE cocktail_id = ci.cocktail_id
        AND ingredient_id IS NOT NULL
    )
),
matching_cocktails_with_spirits AS (
    SELECT cs.cocktail_id
    FROM cocktail_spirits cs
    JOIN available_spirits asps ON cs.spirit_type_id = asps.spirit_type_id
    GROUP BY cs.cocktail_id
    HAVING COUNT(DISTINCT cs.spirit_type_id) = (
        SELECT COUNT(DISTINCT spirit_type_id)
        FROM cocktail_spirits
        WHERE cocktail_id = cs.cocktail_id
        AND spirit_type_id IS NOT NULL
    )
)
SELECT *
FROM (
    SELECT DISTINCT c.*
    FROM matching_cocktails mc
    JOIN cocktails c ON mc.cocktail_id = c.cocktail_id
    UNION
    SELECT DISTINCT c.*
    FROM matching_cocktails_with_spirits mcw
    JOIN cocktails c ON mcw.cocktail_id = c.cocktail_id
) AS combined_results
ORDER BY combined_results.name
""", nativeQuery = true)
    List<Cocktail> findByExactIngredientsAndSpirits(
            @Param("ingredients") String ingredients,
            @Param("spiritTypes") String spiritTypes);









    // Query to find cocktails by spirit type IDs
    @Query("SELECT c FROM Cocktail c " +
            "JOIN c.cocktailSpirits cs " +
            "JOIN cs.spiritType st " +
            "WHERE st.spiritTypeId IN :spiritTypeIds")
    public List<Cocktail> findBySpiritTypeIds(@Param("spiritTypeIds") List<Long> spiritTypeIds);


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
