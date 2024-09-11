package com.fallt.caching_example.service;

import com.fallt.caching_example.dto.request.BookDto;
import com.fallt.caching_example.entity.Book;
import com.fallt.caching_example.entity.Category;
import com.fallt.caching_example.exception.EntityNotFoundException;
import com.fallt.caching_example.mapper.BookMapper;
import com.fallt.caching_example.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {

    private final BookRepository bookRepository;

    private final CategoryService categoryService;

    @Cacheable(value = "BookService::getByTitleAndAuthor", key = "#title + #author")
    public BookDto getByTitleAndAuthor(String title, String author) {
        log.info("Вызов метода getByTitleAndAuthor");
        Optional<Book> optionalBook = bookRepository.findByTitleAndAuthor(title, author);
        if (optionalBook.isEmpty()) {
            throw new EntityNotFoundException("Книга не найдена");
        }
        return BookMapper.INSTANCE.toDto(optionalBook.get());
    }

    @Cacheable(value = "BookService::getBooksByCategoryName", key = "#name")
    public List<BookDto> getBooksByCategoryName(String name) {
        log.info("Вызов метода getBooksByCategoryName");
        return BookMapper.INSTANCE.toListDto(bookRepository.findByCategoryName(name));
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "BookService::getByTitleAndAuthor", allEntries = true),
            @CacheEvict(value = "BookService::getBooksByCategoryName", allEntries = true)
    })
    public BookDto createBook(BookDto request) {
        Category category = categoryService.getCategoryByName(request.getCategory());
        Book book = BookMapper.INSTANCE.toEntity(request);
        book.setCategory(category);
        return BookMapper.INSTANCE.toDto(bookRepository.save(book));
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "BookService::getByTitleAndAuthor", allEntries = true),
            @CacheEvict(value = "BookService::getBooksByCategoryName", allEntries = true)
    })
    public BookDto updateBook(Long id, BookDto dto) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        if (optionalBook.isEmpty()) {
            throw new EntityNotFoundException("Книга не найдена");
        }
        Book updateBook = BookMapper.INSTANCE.updateBookFromDto(dto, optionalBook.get());
        if (dto.getCategory() != null) {
            Category newCategory = categoryService.getCategoryByName(dto.getCategory());
            updateBook.setCategory(newCategory);
        }
        return BookMapper.INSTANCE.toDto(bookRepository.save(updateBook));
    }

    @Caching(evict = {
            @CacheEvict(value = "BookService::getByTitleAndAuthor", allEntries = true),
            @CacheEvict(value = "BookService::getBooksByCategoryName", allEntries = true)
    })
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }
}
