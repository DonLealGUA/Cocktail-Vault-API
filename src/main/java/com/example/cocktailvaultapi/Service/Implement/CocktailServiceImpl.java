package com.example.cocktailvaultapi.Service.Implement;

import com.example.cocktailvaultapi.Model.Cocktail;
import com.example.cocktailvaultapi.Repository.CocktailRepository;
import com.example.cocktailvaultapi.Service.CocktailService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CocktailServiceImpl implements CocktailService {

    private final CocktailRepository cocktailRepository;

    public CocktailServiceImpl(CocktailRepository cocktailRepository) {
        this.cocktailRepository = cocktailRepository;
    }

    @Override
    public List<Cocktail> findAllCocktails() {
        return cocktailRepository.findAll();
    }

    @Override
    public Optional<Cocktail> findByName(String name) {
        return cocktailRepository.findByName(name);
    }

}
