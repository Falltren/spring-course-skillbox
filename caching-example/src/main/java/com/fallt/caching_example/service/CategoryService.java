package com.fallt.caching_example.service;

import com.fallt.caching_example.entity.Category;
import com.fallt.caching_example.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Category getCategoryByName(String name) {
        Category category;
        String categoryNameForDb = name.strip().toLowerCase();
        Optional<Category> optionalCategory = categoryRepository.findByName(categoryNameForDb);
        if (optionalCategory.isEmpty()) {
            Category newCategory = new Category();
            newCategory.setName(categoryNameForDb);
            category = categoryRepository.save(newCategory);
        } else {
            category = optionalCategory.get();
        }
        return category;
    }
}
