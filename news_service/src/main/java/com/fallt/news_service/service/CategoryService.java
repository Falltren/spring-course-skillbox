package com.fallt.news_service.service;

import com.fallt.news_service.exception.BadRequestException;
import com.fallt.news_service.model.Category;
import com.fallt.news_service.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Category getCategory(String title) {
        return new Category();
    }

    public Category save(String title) {
        Optional<Category> optionalCategory = categoryRepository.findByTitle(title);
        if (optionalCategory.isPresent()) {
            return optionalCategory.get();
        }
        Category category = new Category();
        category.setTitle(title);
        return categoryRepository.save(category);
    }

    public Category update(Category category, String newTitle) {
        Optional<Category> optionalCategory = categoryRepository.findByTitle(category.getTitle());
        if (optionalCategory.isEmpty()) {
            throw new BadRequestException(MessageFormat.format("Категория с названием: {0} не существует", category.getTitle()));
        }
        Category existedCategory = optionalCategory.get();
        existedCategory.setTitle(newTitle);
        return categoryRepository.save(existedCategory);
    }

}
