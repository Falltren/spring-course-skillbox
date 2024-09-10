package com.fallt.caching_example.repository;

import com.fallt.caching_example.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByTitleAndAuthor(String title, String author);

    List<Book> findByCategoryName(String name);
}
