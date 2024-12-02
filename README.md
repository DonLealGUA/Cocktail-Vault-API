
# Cocktail Vault API 🍹

**Cocktail-Vault-API** is a robust and easy-to-use Spring Boot application for cocktail enthusiasts and developers. It provides access to an extensive collection of cocktail recipes, ingredients, and preparation instructions. Perfect for mixology apps, bar management tools, or anyone looking to create and explore unique drink ideas! It provides a variety of endpoints to retrieve, filter, and manipulate cocktail data.

## Features

- 🔍 **Search Cocktails**: Fetch all cocktails or a specific one by name, first letter, or ID.
- 📋 **Detailed Recipes**: Access step-by-step preparation instructions.
- 🍸 **Ingredient Database**: Comprehensive list of ingredients and substitutes.
- 📚 **Categories**: Browse cocktails by type (e.g., classics, seasonal, non-alcoholic).
- 🥃 **Filter Options**: Filter cocktails by spirit type, spirit brand, glass type, or ingredient.
- 🎲 **Random Cocktails**: Fetch a random cocktail or multiple random cocktails.
- 🆕 **Latest Cocktails**: Get the newest additions to the cocktail database.
- 📄 **Paginated Results**: Retrieve organized lists of cocktails with pagination.
- 🚀 **RESTful API**: Simple and efficient endpoints for seamless integration.

## Use Cases

- Build your own cocktail app.
- Create a bar inventory management system.
- Learn and explore mixology techniques.

## Getting Started

### Prerequisites

- Java 17 or later
- Maven or Gradle
- Spring Boot
- A compatible database (PostgreSQL)

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/DonLealGUA/Cocktail-Vault-API.git
   ```

2. Navigate to the project directory:
   ```bash
   cd cocktail-vault-api
   ```

3. Build the project using Maven:
   ```bash
   mvn clean install
   ```

4. Run the application:
   ```bash
   mvn spring-boot:run
   ```

## API Endpoints

### Retrieve All Cocktails

- **Endpoint**: `GET /api/cocktails`
- **Description**: Retrieves a list of all cocktails.
- **Response**:
  ```json
  [
       {
        "id": 1,
        "name": "Margarita",
        "instructions": "Shake the tequila, lime juice, and triple sec with ice, then strain into a salted rim glass.",
        "imageUrl": "https://example.com/margarita.jpg",
        "glassType": "Highball Glass",
        "spiritTypes": [
            "Tequila",
            "Triple Sec"
        ],
        "ingredients": [
            {
                "ingredientName": "Lime",
                "quantity": "2 cl",
                "spiritTypeName": null
            },
            {
                "ingredientName": "Sugar Syrup",
                "quantity": "1.5 cl",
                "spiritTypeName": null
            },
            {
                "ingredientName": null,
                "quantity": "1.5 cl",
                "spiritTypeName": "Tequila"
            },
            {
                "ingredientName": null,
                "quantity": "1.5 cl",
                "spiritTypeName": "Triple Sec"
            }
        ],
        "createdDate": "2024-11-26T20:34:50.494+00:00",
        "spiritBrand": "Patrón",
        "iceForm": "Cubed"
    },
      ...
  ]
  ```

### Get Cocktail Count

- **Endpoint**: `GET /api/cocktails/count`
- **Description**: Retrieves the total number of cocktails.
- **Response**: `Total Cocktails: 42`

### Paginated Cocktail Retrieval

- **Endpoint**: `GET /api/cocktails/page`
- **Parameters**:
  - `page`: Page number (default: 0)
  - `size`: Number of items per page (default: 10)
- **Description**: Retrieves a paginated list of cocktails.
- **Example**: `GET /api/cocktails/page?page=1&size=5`

### Get Latest Cocktails

- **Endpoint**: `GET /api/cocktails/latest`
- **Parameters**:
  - `page`: Page number (default: 0)
  - `size`: Number of items per page (default: 10)
- **Description**: Retrieves the latest cocktails.

### Get Random Cocktail

- **Endpoint**: `GET /api/cocktails/random`
- **Description**: Fetches a random cocktail.
- **Example Response**:
  ```json
  {
            "id": 3,
            "name": "Old Fashioned",
            "instructions": "Muddle the sugar, bitters, and water, then add ice and whiskey. Stir and garnish with an orange peel.",
            "imageUrl": "https://example.com/oldfashioned.jpg",
            "glassType": "Old-Fashioned Glass",
            "spiritTypes": [
                "Whiskey"
            ],
            "ingredients": [
                {
                    "ingredientName": "Sugar",
                    "quantity": "1 cube",
                    "spiritTypeName": null
                },
                {
                    "ingredientName": "Angostura Bitters",
                    "quantity": "2 dashes",
                    "spiritTypeName": null
                },
                {
                    "ingredientName": null,
                    "quantity": "5 cl",
                    "spiritTypeName": "Whiskey"
                }
            ],
            "createdDate": "2024-11-26T20:34:50.494+00:00",
            "spiritBrand": "Woodford Reserve",
            "iceForm": "Cubed"
        }
  ```

### Filter by Spirit Brand

- **Endpoint**: `GET /api/cocktails/filter/brand/{brand}`
- **Description**: Fetches cocktails that use the specified spirit brand.

### Filter by Ingredient

- **Endpoint**: `GET /api/cocktails/filter/ingredient/{ingredient}`
- **Description**: Retrieves cocktails containing the specified ingredient.

## Example Usage

1. **Get All Cocktails**
   ```bash
   curl -X GET http://localhost:8080/api/cocktails
   ```

2. **Get Cocktail Count**
   ```bash
   curl -X GET http://localhost:8080/api/cocktails/count
   ```

3. **Fetch Paginated Results**
   ```bash
   curl -X GET "http://localhost:8080/api/cocktails/page?page=1&size=10"
   ```

4. **Search by Name**
   ```bash
   curl -X GET http://localhost:8080/api/cocktails/Mojito
   ```

5. **Get Random Cocktail**
   ```bash
   curl -X GET http://localhost:8080/api/cocktails/random
   ```

## Error Handling

The API uses custom exceptions to handle errors. For example, if no cocktails match a query, a `400 Bad Request` response is returned with a descriptive message.

## Contribution

Contributions are welcome! Please fork the repository and create a pull request.

## License

This project is licensed under the MIT License.
