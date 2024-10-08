package com.fallt.news_service.service;

import com.fallt.news_service.dto.request.CategoryDto;
import com.fallt.news_service.entity.Category;
import com.fallt.news_service.exception.EntityNotFoundException;
import com.fallt.news_service.mapper.CategoryMapper;
import com.fallt.news_service.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Category changeCategory(Category category, String newTitle) {
        Optional<Category> optionalCategory = categoryRepository.findByTitle(category.getTitle());
        if (optionalCategory.isEmpty()) {
            throw new EntityNotFoundException(MessageFormat.format("Категория с названием: {0} не существует", category.getTitle()));
        }
        Category existedCategory = optionalCategory.get();
        existedCategory.setTitle(newTitle);
        return categoryRepository.save(existedCategory);
    }

    public CategoryDto update(CategoryDto categoryDto, Long id) {
        Category category = getCategory(id);
        CategoryMapper.INSTANCE.updateCategoryFromDto(categoryDto, category);
        return CategoryMapper.INSTANCE.toDto(categoryRepository.save(category));
    }

    public CategoryDto getCategoryById(Long id){
        return CategoryMapper.INSTANCE.toDto(getCategory(id));
    }

    private Category getCategory(Long id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isEmpty()) {
            throw new EntityNotFoundException(MessageFormat.format("Категория с ID: {0} не существует", id));
        }
        return optionalCategory.get();
    }

    public List<CategoryDto> getAllCategories(Integer offset, Integer limit) {
        return CategoryMapper.INSTANCE.toListDto(categoryRepository.findAll(PageRequest.of(offset, limit)).getContent());
    }

    public CategoryDto createCategory(CategoryDto categoryDto) {
        return CategoryMapper.INSTANCE.toDto(createOrGetExist(categoryDto));
    }

    public Category createOrGetExist(CategoryDto categoryDto) {
        Optional<Category> optionalCategory = categoryRepository.findByTitle(categoryDto.getTitle());
        if (optionalCategory.isPresent()) {
            return optionalCategory.get();
        }
        Category category = new Category();
        category.setTitle(categoryDto.getTitle());
        return categoryRepository.save(category);
    }

    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }

}
