package com.example.cocktailvaultapi.Service;

import com.example.cocktailvaultapi.DTO.CocktailDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public interface CocktailService {
    List<CocktailDTO> findAllCocktails();
    ResponseEntity<CocktailDTO> findByName(String name); // Update return type to ResponseEntity

}
