package com.fallt.news_service.repository;

import com.fallt.news_service.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByTitle(String title);
}
