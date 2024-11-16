package com.example.cocktailvaultapi.Repository;

import com.example.cocktailvaultapi.Model.Cocktail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CocktailRepository extends JpaRepository<Cocktail, Long> {

    //TODO
    // Få alla case insesetive

    /**
     * Search for a cocktail by its name, ignoring case.
     * @param name the name of the cocktail to search for.
     * @param pageable the pagination information (page number, page size, sorting).
     * @return an Optional containing the found cocktail, if any.
     */
    @Query("SELECT c FROM Cocktail c WHERE REPLACE(LOWER(c.name), ' ', '') LIKE REPLACE(LOWER(:name), ' ', '')")
    Page<Cocktail> findByNameIgnoreCaseAndSpaceInsensitive(@Param("name") String name, Pageable pageable);


    /**
     * Returns the amount of cocktails in DB
     */
    @Query("select COUNT(*) from Cocktail")
    String countTotalCocktails();

    /**
     * List all cocktails whose names start with a specific letter, ignoring case.
     * @param letter the starting letter to filter cocktails by.
     * @param pageable the pagination information (page number, page size, sorting).
     * @return a list of cocktails that start with the specified letter.
     */
    Page<Cocktail> findByNameStartingWithIgnoreCase(String letter,Pageable pageable); // List all cocktails starting with a specific letter

    /**
     * Get a random cocktail from the database.
     * @return an Optional containing a randomly selected cocktail.
     */
    @Query(value = "SELECT * FROM cocktails ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    Optional<Cocktail> getRandomCocktail(); // Get a random cocktail

    @Query("SELECT c FROM Cocktail c ORDER BY RANDOM()")
    Page<Cocktail> getMultiRandCocktails(Pageable pageable);



    /**
     * Retrieve the latest cocktails added to the database, sorted by creation date.
     * @param pageable the pagination information.
     * @return a list of the latest cocktails.
     */
    @Query("SELECT c FROM Cocktail c ORDER BY c.createdDate DESC")
    Page<Cocktail> findLatestCocktails(Pageable pageable); // List the latest cocktails added

    /**
     * Find cocktails based on a specific spirit brand, ignoring case.
     * @param brand the spirit brand to filter by (e.g., "Absolut").
     * @param pageable the pagination information (page number, page size, sorting).
     * @return a list of cocktails containing the specified spirit brand.
     */
    Page<Cocktail> findBySpiritBrandIgnoreCase(String brand,Pageable pageable); // Get recipes by specific spirit brand (e.g., Absolut)

    /**
     * Search for cocktails by their spirit type, ignoring case and spaces in the spirit type name.
     * @param spiritType the spirit type to filter cocktails by (e.g., "Vodka").
     * @param pageable the pagination information (page number, page size, sorting).
     * @return a list of cocktails containing the specified spirit type.
     */
    @Query("SELECT c FROM Cocktail c JOIN c.cocktailSpirits cs JOIN cs.spiritType st WHERE LOWER(st.name) = LOWER(:spiritType)")
    Page<Cocktail> findBySpiritTypeIgnoreCaseAndSpaces(@Param("spiritType") String spiritType, Pageable pageable); // Search cocktails by spirit type

    /**
     * Filter cocktails based on their glass type, ignoring case and spaces in the glass type name.
     * @param glassType the glass type to filter cocktails by (e.g., "Martini glass").
     * @param pageable the pagination information (page number, page size, sorting).
     * @return a list of cocktails that use the specified glass type.
     */
    @Query("SELECT c FROM Cocktail c JOIN c.glassType g WHERE LOWER(REPLACE(g.name, ' ', '')) = LOWER(REPLACE(:glassType, ' ', ''))")
    Page<Cocktail> findByGlassTypeIgnoreCaseAndSpaces(@Param("glassType") String glassType, Pageable pageable); // Filter cocktails by glass type

    /**
     * Find cocktails containing a specific ingredient, ignoring case and spaces in the ingredient name.
     * @param ingredientName the ingredient name to filter cocktails by (e.g., "Lime").
     * @param pageable the pagination information (page number, page size, sorting).
     * @return a list of cocktails that contain the specified ingredient.
     */
    @Query("SELECT c FROM Cocktail c " + "JOIN c.cocktailIngredients ci " + "JOIN ci.ingredient i " + "WHERE LOWER(REPLACE(i.name, ' ', '')) = LOWER(REPLACE(:ingredientName, ' ', ''))")
    Page<Cocktail>findCocktailsByIngredientNameIgnoreCaseAndSpaces(@Param("ingredientName") String ingredientName, Pageable pageable);



    /**
     * Retrieves cocktails that contain only the specified ingredients, with no additional ingredients.
     * Ingredients types are provided as comma-separated, normalized list.
     * The cocktails must exactly match the provided ingredients, but they do not have to include every specified ingredient or spirit.
     *
     * @param ingredients Comma-separated, normalized list of ingredient names.
     * @param pageable Pagination information to manage the size and number of results.
     * @return A paginated list of cocktails that match the exact specified ingredients and spirits.
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
    Page<Cocktail> findCocktailsWithExactIngredients(List<String> ingredients,Pageable pageable);


    /**
     * Retrieves cocktails that contain only the specified ingredients and spirit types, with no additional ingredients or spirits.
     * Ingredients and spirit types are provided as comma-separated, normalized lists.
     * The cocktails must exactly match the provided ingredients and spirit types, but they do not have to include every specified ingredient or spirit.
     *
     * @param ingredients Comma-separated, normalized list of ingredient names.
     * @param spiritTypes Comma-separated, normalized list of spirit types.
     * @param pageable Pagination information to manage the size and number of results.
     * @return A paginated list of cocktails that match the exact specified ingredients and spirits.
     */
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
    Page<Cocktail> findByExactIngredientsAndSpirits(
            @Param("ingredients") String ingredients,
            @Param("spiritTypes") String spiritTypes,Pageable pageable);


    /**
     * Retrieves cocktails that match at least 50% of the specified ingredients.
     * Ingredients are provided as a comma-separated, normalized list.
     *
     * @param ingredients Comma-separated, normalized list of ingredient names.
     * @param pageable Pagination information to manage the size and number of results.
     * @return A paginated list of cocktails that match at least 50% of the provided ingredients.
     */
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
    Page<Cocktail> findByPartialIngredients(@Param("ingredients") String ingredients,Pageable pageable);


    /**
     * Retrieves cocktails that match at least 50% of the specified ingredients and, if provided, spirits.
     * Ingredients and spirits are provided as comma-separated, normalized lists.
     *
     * @param ingredients Comma-separated, normalized list of ingredient names.
     * @param spirits Comma-separated, normalized list of spirit types.
     * @param pageable Pagination information to manage the size and number of results.
     * @return A paginated list of cocktails that match at least 50% of the specified ingredients and spirits.
     */
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
    Page<Cocktail> findByPartialIngredientsAndSpirits(@Param("ingredients") String ingredients, @Param("spiritTypes") String spirits,Pageable pageable);


}
