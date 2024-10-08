package com.fallt.news_service.mapper;

import com.fallt.news_service.dto.request.CategoryDto;
import com.fallt.news_service.entity.Category;
import org.mapstruct.*;

import java.util.List;

import static org.mapstruct.factory.Mappers.getMapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryMapper INSTANCE = getMapper(CategoryMapper.class);

    CategoryDto toDto(Category category);

    @Mapping(target = "id", ignore = true)
    Category toEntity(CategoryDto categoryDto);

    List<CategoryDto> toListDto(List<Category> categories);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCategoryFromDto(CategoryDto categoryDto, @MappingTarget Category category);
}
