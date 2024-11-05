package com.example.cocktailvaultapi.Controller;

import com.example.cocktailvaultapi.DTO.CocktailDTO;
import com.example.cocktailvaultapi.Service.CocktailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cocktails")
public class CocktailController {

    private final CocktailService cocktailService; // Use the interface instead of the implementation

    public CocktailController(CocktailService cocktailService) {
        this.cocktailService = cocktailService;
    }

    @GetMapping
    public List<CocktailDTO> getAllCocktails() {
        return cocktailService.findAllCocktails();
    }

    @GetMapping("/{name}")
    public ResponseEntity<CocktailDTO> getCocktailByName(@PathVariable("name") String name) {
        return cocktailService.findByName(name);
    }
}
