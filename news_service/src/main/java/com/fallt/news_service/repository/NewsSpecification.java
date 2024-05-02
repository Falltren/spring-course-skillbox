package com.fallt.news_service.repository;

import com.fallt.news_service.dto.request.NewsFilter;
import com.fallt.news_service.model.Category;
import com.fallt.news_service.model.News;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public interface NewsSpecification {

    static Specification<News> withFilter(NewsFilter filter) {
        return Specification.where(byCategory(filter.getCategory()))
                .and(byAuthor(filter.getAuthor()));
    }

    static Specification<News> byCategory(String category) {
        return ((root, query, criteriaBuilder) -> {
            if (category == null) {
                return null;
            }
            Join<News, Category> newsCategoryJoin = root.join("category_id");
            return criteriaBuilder.equal(newsCategoryJoin.get("categories.title"), category);
        });
    }

    static Specification<News> byAuthor(String author) {
        return ((root, query, criteriaBuilder) -> {
            if (author == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("user"), author);
        });
    }
}
