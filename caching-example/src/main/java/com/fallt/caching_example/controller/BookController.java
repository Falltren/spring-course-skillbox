package com.fallt.caching_example.controller;

import com.fallt.caching_example.dto.request.BookDto;
import com.fallt.caching_example.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/book")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    public BookDto findBookByTitleAndAuthor(@RequestParam String title, @RequestParam String author) {
        return bookService.getByTitleAndAuthor(title, author);
    }

    @GetMapping("/category")
    public List<BookDto> getBooksByCategoryName(@RequestParam String categoryName) {
        return bookService.getBooksByCategoryName(categoryName);
    }

    @PostMapping()
    public BookDto createBook(@RequestBody BookDto dto) {
        return bookService.createBook(dto);
    }

    @PutMapping("/{id}")
    public BookDto updateBook(@RequestBody BookDto dto, @PathVariable Long id) {
        return bookService.updateBook(id, dto);
    }

    @DeleteMapping("{/id}")
    public void deleteById(@PathVariable Long id) {
        bookService.deleteById(id);
    }

}
