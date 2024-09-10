package com.fallt.caching_example.mapper;

import com.fallt.caching_example.dto.request.BookDto;
import com.fallt.caching_example.entity.Book;
import org.mapstruct.*;

import java.util.List;

import static org.mapstruct.factory.Mappers.getMapper;

@Mapper(componentModel = "spring")
public interface BookMapper {

    BookMapper INSTANCE = getMapper(BookMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    Book toEntity(BookDto request);

    @Mapping(target = "category", expression = "java(book.getCategory().getName())")
    BookDto toDto(Book book);

    List<BookDto> toListDto(List<Book> books);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Book updateBookFromDto(BookDto dto, @MappingTarget Book book);
}
