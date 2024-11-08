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
    // The cocktails need to contain exactly the ingredients you provide (but not necessarily all of them),
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


//==================================================================================================

    // Repository Layer: Query for partial ingredient matches (at least 2 ingredients)
    @Query(value = """
WITH available_ingredients AS (
    SELECT ingredient_id
    FROM ingredients i
    JOIN LATERAL unnest(string_to_array(:ingredients, ',')) AS ingredient_name ON TRUE
    WHERE TRIM(LOWER(REPLACE(i.name, ' ', ''))) = LOWER(REPLACE(ingredient_name, ' ', ''))
    AND TRIM(LOWER(REPLACE(i.type, ' ', ''))) NOT IN (LOWER(REPLACE('garnish', ' ', '')))
),

matching_cocktails AS (
    SELECT ci.cocktail_id
    FROM cocktail_ingredients ci
    JOIN available_ingredients ai ON ci.ingredient_id = ai.ingredient_id
    WHERE ci.ingredient_id IS NOT NULL  -- Exclude NULL ingredients
    GROUP BY ci.cocktail_id
    HAVING 
        -- Ensure at least 50% of the ingredients in the cocktail match
        COUNT(DISTINCT ci.ingredient_id) >= (
            SELECT COUNT(DISTINCT ci2.ingredient_id) / 2 -- 50% of the total ingredients in the cocktail
            FROM cocktail_ingredients ci2
            WHERE ci2.cocktail_id = ci.cocktail_id
            AND ci2.ingredient_id IS NOT NULL
        )
)

-- Return only distinct cocktail names based on matching ingredients
 SELECT DISTINCT c.*
FROM cocktails c
JOIN matching_cocktails mc ON c.cocktail_id = mc.cocktail_id
ORDER BY c.name;
""", nativeQuery = true)
    List<Cocktail> findByPartialIngredients(@Param("ingredients") String ingredients);











    // Repository Layer: Query for partial ingredient and spirit matches
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
    LEFT JOIN available_spirits asps ON ci.spirit_type_id = asps.spirit_type_id
    WHERE ci.ingredient_id IS NOT NULL
    GROUP BY ci.cocktail_id
    HAVING 
        -- Ensure at least 50% of the ingredients match
        COUNT(DISTINCT ci.ingredient_id) >= (
            SELECT COUNT(DISTINCT ci2.ingredient_id) / 2 -- 50% of the total ingredients in the cocktail
            FROM cocktail_ingredients ci2
            WHERE ci2.cocktail_id = ci.cocktail_id
            AND ci2.ingredient_id IS NOT NULL
        )
        -- Ensure the cocktail only requires spirits if spirits are available
        AND (
            (SELECT COUNT(*) FROM available_spirits) > 0  -- If spirits are provided, check for matching spirits
            OR NOT EXISTS (  -- If no spirits are provided, ensure no spirits are required in the cocktail
                SELECT 1 
                FROM cocktail_ingredients 
                WHERE cocktail_id = ci.cocktail_id 
                AND spirit_type_id IS NOT NULL
            )
        )
),
matching_cocktails_with_spirits AS (
    SELECT cs.cocktail_id
    FROM cocktail_spirits cs
    JOIN available_spirits asps ON cs.spirit_type_id = asps.spirit_type_id
    WHERE cs.spirit_type_id IS NOT NULL
    GROUP BY cs.cocktail_id
    HAVING COUNT(DISTINCT cs.spirit_type_id) = (
        SELECT COUNT(DISTINCT spirit_type_id)
        FROM cocktail_spirits
        WHERE cocktail_id = cs.cocktail_id
        AND spirit_type_id IS NOT NULL
    )
)

-- Return distinct cocktail names that match both the ingredients and spirits
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
    List<Cocktail> findByPartialIngredientsAndSpirits(@Param("ingredients") String ingredients, @Param("spiritTypes") String spirits);




}
