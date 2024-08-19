package com.fallt.news_service.controller;

import com.fallt.news_service.dto.request.CategoryDto;
import com.fallt.news_service.mapper.CategoryMapper;
import com.fallt.news_service.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/{id}")
    public CategoryDto getCategory(@PathVariable Long id) {
        return CategoryMapper.INSTANCE.toDto(categoryService.getCategoryById(id));
    }

    @GetMapping
    public List<CategoryDto> getAllCategories(@RequestParam Integer offset, @RequestParam Integer limit) {
        return CategoryMapper.INSTANCE.toListDto(categoryService.getAllCategories(offset, limit));
    }

    @PostMapping("/create")
    public CategoryDto create(@Valid @RequestBody CategoryDto categoryDto) {
        return CategoryMapper.INSTANCE.toDto(categoryService.create(categoryDto));
    }

    @PutMapping("/{id}")
    public CategoryDto update(@RequestBody CategoryDto categoryDto, @PathVariable Long id) {
        return CategoryMapper.INSTANCE.toDto(categoryService.update(categoryDto, id));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        categoryService.delete(id);
    }
}
